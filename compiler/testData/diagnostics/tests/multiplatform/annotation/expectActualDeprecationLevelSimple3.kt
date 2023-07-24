// FIR_IDENTICAL
// WITH_STDLIB
// MODULE: m1-common
// FILE: common.kt
@Deprecated("message", level = DeprecationLevel.ERROR)
expect fun foo()

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt
@Deprecated("message", level = DeprecationLevel.ERROR)
actual fun foo() {}