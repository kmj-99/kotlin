/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.low.level.api.fir.caches

import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

/**
 * [SoftValueCleaner] performs a cleaning operation after its associated value has been removed from the cache or garbage-collected. The
 * cleaner will be strongly referenced from the soft references held by the cache.
 *
 * You **must not** store a reference to the associated value [V] in its [SoftValueCleaner]. Otherwise, the cached values will never become
 * softly reachable.
 *
 * The cleaner may be invoked multiple times by the cache, in any thread. Implementations of [SoftValueCleaner] must ensure that the
 * operation is repeatable and thread-safe.
 */
internal fun interface SoftValueCleaner<V> {
    fun cleanUp(value: V?)
}

/**
 * A cache with hard references to its keys [K] and soft references to its values [V].
 *
 * Each value of the cache has a [SoftValueCleaner] associated with it. The cache ensures that this cleaner is invoked when the value is
 * removed from or replaced in the cache, or when the value has been garbage-collected. Already collected values from the cache's reference
 * queue are guaranteed to be processed on mutating operations (such as `put`, `remove`, and so on). The [SoftValueCleaner] will be strongly
 * referenced from the cache until collected values have been processed.
 *
 * `null` keys or values are not allowed.
 *
 * The cache is loosely based on [com.intellij.util.containers.ConcurrentRefValueHashMap].
 *
 * @param getCleaner Returns the [SoftValueCleaner] that should be invoked after [V] has been collected or removed from the cached. The
 *  function will be invoked once when the value is added to the cache.
 */
internal class CleanableSoftValueCache<K : Any, V : Any>(
    private val getCleaner: (V) -> SoftValueCleaner<V>,
) {
    /**
     * The backing map needs to be an [AtomicReference] to support atomic [clear] without synchronization.
     */
    private val backingMapReference = AtomicReference(ConcurrentHashMap<K, SoftReferenceWithCleanup<K, V>>())

    private val backingMap get() = backingMapReference.get()

    private val referenceQueue = ReferenceQueue<V>()

    private fun processQueue() {
        while (true) {
            @Suppress("UNCHECKED_CAST")
            val ref = referenceQueue.poll() as? SoftReferenceWithCleanup<K, V> ?: break
            backingMap.remove(ref.key, ref)
            ref.performCleanup()
        }
    }

    fun get(key: K): V? = backingMap[key]?.get()

    /**
     * If [key] is currently absent, attempts to add a value computed by [f] to the cache. [f] will not be invoked if [key] is present.
     *
     * The implementation is not atomic with respect to [f], i.e. the value computation may be run concurrently on multiple threads if more
     * than one thread calls [computeIfAbsent] for the same [key]. However, the implementation guarantees that the value eventually returned
     * from [computeIfAbsent] is consistent across all calling threads.
     */
    fun computeIfAbsent(key: K, f: (K) -> V): V {
        get(key)?.let { return it }

        val newValue = f(key)
        putIfAbsent(key, newValue)?.let { return it }

        return newValue
    }

    /**
     * @return The old value that has been replaced, if any. As replacement constitutes removal, the cleaner associated with the value will
     * be invoked by [put].
     */
    fun put(key: K, value: V): V? {
        val oldRef = backingMap.put(key, createSoftReference(key, value))
        oldRef?.performCleanup()

        processQueue()
        return oldRef?.get()
    }

    fun putIfAbsent(key: K, value: V): V? {
        val newRef = createSoftReference(key, value)
        while (true) {
            val currentRef = backingMap.putIfAbsent(key, newRef)
            processQueue()
            if (currentRef == null) return null

            // If `currentRef` exists but its value has already been collected, to the outside it should look like no value existed in the
            // cache and `putIfAbsent` should succeed.
            val currentValue = currentRef.get()
            if (currentValue == null) {
                val wasReplaced = backingMap.replace(key, currentRef, newRef)
                if (wasReplaced) {
                    // In most cases, `processQueue` will probably already have invoked the ref's cleaner. However, if the referent is
                    // collected between `processQueue()` and `currentRef.get()`, it won't have been cleaned yet, and we can invoke the
                    // cleaner here. The reference will later be processed by `processQueue`, but that is fine because cleaners can be
                    // invoked multiple times.
                    currentRef.performCleanup()
                    return null
                }
            } else {
                return currentValue
            }
        }
    }

    fun remove(key: K): V? {
        val ref = backingMap.remove(key)
        ref?.performCleanup()

        processQueue()
        return ref?.get()
    }

    fun clear() {
        // We need to perform cleanup for *exactly* the values that are cleared from the cache. Unfortunately, `ConcurrentMap` doesn't
        // support an atomic "clear and get values". Another way to approach this issue is to swap out the backing map atomically, because
        // clearing and getting values from the old map doesn't need to happen atomically, as there will be no further mutations of the map.
        //
        // We use `AtomicReference` here to avoid several `clear` calls interfering with each other. If we had separate `get` and `set`
        // operations, two `clear` calls could both in sequence: (1) Get the old map, (2) perform cleanup for the map's values, (3) clear
        // the map. Because the first `clear` call might happen *after* both threads have performed cleanup for the values, this leads to
        // duplicate cleanup, which is legal, but wasteful.
        val oldMap = backingMapReference.getAndSet(ConcurrentHashMap())
        oldMap.values.forEach { it.performCleanup() }
        oldMap.clear()

        processQueue()
    }

    val size: Int
        get() {
            processQueue()
            return backingMap.size
        }

    fun isEmpty(): Boolean {
        processQueue()
        return backingMap.isEmpty()
    }

    /**
     * Returns a snapshot of all keys in the cache. Changes to the cache do not reflect in the resulting set.
     */
    val keys: Set<K> get() = backingMap.keys.toSet()

    override fun toString(): String = "${this::class.simpleName} size:$size"

    private fun createSoftReference(key: K, value: V) = SoftReferenceWithCleanup(key, value, getCleaner(value), referenceQueue)

    private fun SoftReferenceWithCleanup<K, V>.performCleanup() {
        cleaner.cleanUp(get())
    }
}

/**
 * @see com.intellij.util.containers.ConcurrentSoftValueHashMap
 */
private class SoftReferenceWithCleanup<K, V>(
    val key: K,
    value: V,
    val cleaner: SoftValueCleaner<V>,
    referenceQueue: ReferenceQueue<V>,
) : SoftReference<V>(value, referenceQueue) {
    override fun equals(other: Any?): Boolean {
        // When the referent is collected, equality should be identity-based (for `processQueue` to remove this very same soft value).
        // Hence, we skip the value equality check if the referent has been collected and `get()` returns `null`. If the reference is still
        // valid, this is just a canonical equals on referents for `replace(K,V,V)`.
        //
        // The `cleaner` is not part of equality, because `value` equality implies `cleaner` equivalence.
        if (this === other) return true
        if (other == null || other !is SoftReferenceWithCleanup<*, *>) return false
        if (key != other.key) return false

        val value = get() ?: return false
        return value == other.get()
    }

    override fun hashCode(): Int = key.hashCode()
}
