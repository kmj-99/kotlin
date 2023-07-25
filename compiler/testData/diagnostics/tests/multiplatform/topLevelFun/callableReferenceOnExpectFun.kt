// WITH_STDLIB
// MODULE: m1-common
// FILE: common.kt
@Deprecated("message", level = DeprecationLevel.ERROR)
expect fun foo(): String

fun g(f: () -> String): String = f()

fun test() {
    g(::<!DEPRECATION{JVM}, DEPRECATION_ERROR!>foo<!>)
}

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt
@Deprecated("To check that ::foo is resolved to actual fun foo when compiling common+jvm", level = DeprecationLevel.WARNING)
actual fun foo(): String = ""
