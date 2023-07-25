// FIR_IDENTICAL
// WITH_STDLIB
// MODULE: m1-common
// FILE: common.kt
expect fun foo()

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt
<!EXPECT_ACTUAL_DEPRECATION_LEVEL!>@Deprecated("message", level = DeprecationLevel.ERROR)<!>
actual fun foo() {}