/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package org.jetbrains.kotlin.name

object SpecialNames {
    @JvmField
    val NO_NAME_PROVIDED = Name.special("<no name provided>")

    @JvmField
    val ROOT_PACKAGE = Name.special("<root package>")

    @JvmField
    val DEFAULT_NAME_FOR_COMPANION_OBJECT = Name.identifier("Companion")

    @Deprecated("Obsolete. Should be replaced with NO_NAME_PROVIDED", ReplaceWith("NO_NAME_PROVIDED"))
    @JvmField
    val SAFE_IDENTIFIER_FOR_NO_NAME = NO_NAME_PROVIDED

    const val ANONYMOUS_STRING = "<anonymous>"

    @JvmField
    val ANONYMOUS = Name.special(ANONYMOUS_STRING)

    @JvmField
    val UNARY = Name.special("<unary>")

    @JvmField
    val THIS = Name.special("<this>")

    @JvmField
    val INIT = Name.special("<init>")

    @JvmField
    val ITERATOR = Name.special("<iterator>")

    @JvmField
    val DESTRUCT = Name.special("<destruct>")

    @JvmField
    val LOCAL = Name.special("<local>")

    @JvmField
    val UNDERSCORE_FOR_UNUSED_VAR = Name.special("<unused var>")

    @JvmField
    val IMPLICIT_SET_PARAMETER = Name.special("<set-?>")

    @JvmField
    val ARRAY = Name.special("<array>")

    @JvmField
    val RECEIVER = Name.special("<receiver>")

    /**
     * Kotlin-generated `entries` read-only property
     */
    @JvmField
    val ENUM_GET_ENTRIES = Name.special("<get-entries>")

    /**
     * Special name to store expressions used as indexes in subscription operators (`get` and `set`).
     *
     * For example, `bar1()` and `bar2()` are indexes in `foo[bar1(), bar2()]` call.
     */
    @JvmStatic
    fun subscribeOperatorIndex(idx: Int): Name {
        require(idx >= 0) { "Index should be non-negative, but was $idx" }

        return Name.special("<index_$idx>")
    }

    @JvmStatic
    fun safeIdentifier(name: Name?): Name {
        return if (name != null && !name.isSpecial) name else NO_NAME_PROVIDED
    }

    @JvmStatic
    fun safeIdentifier(name: String?): Name {
        return safeIdentifier(if (name == null) null else Name.identifier(name))
    }

    fun isSafeIdentifier(name: Name): Boolean {
        return name.asString().isNotEmpty() && !name.isSpecial
    }
}
