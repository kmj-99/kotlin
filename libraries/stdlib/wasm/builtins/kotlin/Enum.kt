/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin

public abstract class Enum<E : Enum<E>>(
    @kotlin.internal.IntrinsicConstEvaluation
    public val name: String,
    public val ordinal: Int
) : Comparable<E> {

    public override final fun compareTo(other: E): Int =
        ordinal.compareTo(other.ordinal)

    public override final fun equals(other: Any?): Boolean =
        this === other

    public override final fun hashCode(): Int =
        super.hashCode()

    public override fun toString(): String =
        name

    public companion object
}