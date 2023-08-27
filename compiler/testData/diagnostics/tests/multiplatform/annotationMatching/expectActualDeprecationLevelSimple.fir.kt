// ISSUE: KT-60523
// LANGUAGE: -ExpectActualMustHaveSameDeprecationLevel
// WITH_STDLIB
// MODULE: m1-common
// FILE: common.kt
@Deprecated("message", level = DeprecationLevel.WARNING)
expect fun foo()

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt
<!EXPECT_ACTUAL_DEPRECATION_LEVEL_WARNING!>@Deprecated("message", level = DeprecationLevel.ERROR)<!>
actual fun foo() {}