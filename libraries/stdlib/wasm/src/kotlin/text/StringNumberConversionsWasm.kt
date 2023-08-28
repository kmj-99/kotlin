/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin.text

import kotlin.math.abs
import kotlin.wasm.internal.WasmCharArray
import kotlin.wasm.internal.copyWasmArray
import kotlin.wasm.internal.wasm_f32_demote_f64

/**
 * Returns `true` if the contents of this string is equal to the word "true", ignoring case, and `false` otherwise.
 *
 * There are also strict versions of the function available on non-nullable String, [toBooleanStrict] and [toBooleanStrictOrNull].
 */
actual fun String?.toBoolean(): Boolean = this != null && this.lowercase() == "true"

/**
 * Parses the string as a signed [Byte] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 */
actual fun String.toByte(): Byte = toByteOrNull() ?: numberFormatError(this)

/**
 * Parses the string as a signed [Byte] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 * @throws IllegalArgumentException when [radix] is not a valid radix for string to number conversion.
 */
public actual fun String.toByte(radix: Int): Byte = toByteOrNull(radix) ?: numberFormatError(this)

/**
 * Parses the string as a [Short] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 */
public actual fun String.toShort(): Short = toShortOrNull() ?: numberFormatError(this)

/**
 * Parses the string as a [Short] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 * @throws IllegalArgumentException when [radix] is not a valid radix for string to number conversion.
 */
public actual fun String.toShort(radix: Int): Short = toShortOrNull(radix) ?: numberFormatError(this)

/**
 * Parses the string as an [Int] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 */
public actual fun String.toInt(): Int = toIntOrNull() ?: numberFormatError(this)

/**
 * Parses the string as an [Int] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 * @throws IllegalArgumentException when [radix] is not a valid radix for string to number conversion.
 */
public actual fun String.toInt(radix: Int): Int = toIntOrNull(radix) ?: numberFormatError(this)

/**
 * Parses the string as a [Long] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 */
public actual fun String.toLong(): Long = toLongOrNull() ?: numberFormatError(this)

/**
 * Parses the string as a [Long] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 * @throws IllegalArgumentException when [radix] is not a valid radix for string to number conversion.
 */
public actual fun String.toLong(radix: Int): Long = toLongOrNull(radix) ?: numberFormatError(this)

/**
 * Parses the string as a [Double] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 */
public actual fun String.toDouble(): Double = kotlin.text.parseDouble(this)

/**
 * Parses the string as a [Float] number and returns the result.
 * @throws NumberFormatException if the string is not a valid representation of a number.
 */
public actual fun String.toFloat(): Float = wasm_f32_demote_f64(toDouble())

/**
 * Parses the string as a [Float] number and returns the result
 * or `null` if the string is not a valid representation of a number.
 */
public actual fun String.toFloatOrNull(): Float? = toDoubleOrNull()?.let { wasm_f32_demote_f64(it) }

/**
 * Parses the string as a [Double] number and returns the result
 * or `null` if the string is not a valid representation of a number.
 */
public actual fun String.toDoubleOrNull(): Double? {
    try {
        return toDouble()
    } catch (e: NumberFormatException) {
        return null
    }
}

/**
 * Returns a string representation of this [Byte] value in the specified [radix].
 *
 * @throws IllegalArgumentException when [radix] is not a valid radix for number to string conversion.
 */
@SinceKotlin("1.2")
public actual fun Byte.toString(radix: Int): String = this.toLong().toString(radix)

/**
 * Returns a string representation of this [Short] value in the specified [radix].
 *
 * @throws IllegalArgumentException when [radix] is not a valid radix for number to string conversion.
 */
@SinceKotlin("1.2")
public actual fun Short.toString(radix: Int): String = this.toLong().toString(radix)

/**
 * Returns a string representation of this [Int] value in the specified [radix].
 *
 * @throws IllegalArgumentException when [radix] is not a valid radix for number to string conversion.
 */
@SinceKotlin("1.2")
actual fun Int.toString(radix: Int): String = toLong().toString(radix)

/**
 * Returns a string representation of this [Long] value in the specified [radix].
 *
 * @throws IllegalArgumentException when [radix] is not a valid radix for number to string conversion.
 */
@SinceKotlin("1.2")
actual fun Long.toString(radix: Int): String {
    checkRadix(radix)

    if (radix == 10) return toString()
    if (this in 0 until radix) return getChar().toString()

    val isNegative = this < 0
    val buffer = WasmCharArray(Long.SIZE_BITS + 1)

    var currentBufferIndex = Long.SIZE_BITS
    var current: Long = this
    while (current != 0L) {
        buffer.set(currentBufferIndex, abs(current % radix).getChar())
        current /= radix
        currentBufferIndex--
    }

    if (isNegative) {
        buffer.set(currentBufferIndex, '-')
        currentBufferIndex--
    }

    return buffer.createStringStartingFrom(currentBufferIndex + 1)
}

internal fun WasmCharArray.createStringStartingFrom(index: Int): String {
    if (index == 0) return createString()
    val newLength = this.len() - index
    if (newLength == 0) return ""
    val newChars = WasmCharArray(newLength)
    copyWasmArray(this, newChars, index, 0, newLength)
    return newChars.createString()
}

internal fun Long.getChar() = toInt().let { if (it < 10) '0' + it else 'a' + (it - 10) }

