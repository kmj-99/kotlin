// MODULE: m1-common
// FILE: common.kt
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Ann

expect fun inMethod(@Ann arg: String)

expect class InConstructor(@Ann arg: String)

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt
<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>inMethod<!>(arg: String) {}<!>

actual class InConstructor <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT, ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual constructor(arg: String)<!> {}
