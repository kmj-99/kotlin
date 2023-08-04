// MODULE: m1-common
// FILE: common.kt
@Target(AnnotationTarget.TYPE)
annotation class Ann

expect fun valueParameterType(arg: @Ann String)

expect fun returnType(): @Ann String

expect fun <T : @Ann Any> methodTypeParamBound()

expect class OnClassTypeParamBound<T : @Ann Any>

expect fun <T> typeParamBoundInWhere() where T : @Ann Any

interface I1
interface I2

expect fun <T> severalBounds() where T : I1, T : @Ann I2

expect fun @Ann Any.onReceiver()

expect class OnClassSuper : @Ann I1

expect class OnClassSuperDifferentOrder : I1, @Ann I2

expect class OnClassSuperMoreOnActual : @Ann I2

interface I3<T>

expect class OnClassSuperTypeParams<T> : I3<@Ann T>

expect fun deepInParamsTypes(arg: I3<I3<@Ann Any>>)

interface I4<T, U>

expect fun starProjection(arg: I4<*, @Ann Any>)

expect fun <T> typeArgWithVariance(t: I3<out @Ann T>)

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt
<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>valueParameterType<!>(arg: String) {}<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>returnType<!>(): String = ""<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <T : Any> <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>methodTypeParamBound<!>() {}<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual class <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>OnClassTypeParamBound<!><T : Any><!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <T> <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>typeParamBoundInWhere<!>() where T : Any {}<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <T> <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>severalBounds<!>() where T : I1, T : I2 {}<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun Any.<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>onReceiver<!>() {}<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual class <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>OnClassSuper<!> : I1<!>

actual class OnClassSuperDifferentOrder : @Ann I2, I1

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual class <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>OnClassSuperMoreOnActual<!> : I1, I2<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual class <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>OnClassSuperTypeParams<!><T> : I3<T><!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>deepInParamsTypes<!>(arg: I3<I3<Any>>) {}<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>starProjection<!>(arg: I4<*, Any>) {}<!>

<!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>actual fun <T> <!ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT!>typeArgWithVariance<!>(t: I3<out T>) {}<!>
