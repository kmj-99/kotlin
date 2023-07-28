class OutBox0<out T>(val t: T)
val a = OutBox0("")

class Box1<T>(val t: T)

typealias OutBox1<T> = Box1<out T>
typealias InBox1<T> = Box1<in T>
typealias StarBox1 = Box1<*>

val out1 = OutBox1("") // K1: KT-60790
val in1 = InBox1("")
val star1 = StarBox1("") // K1: KT-60790

fun acceptInvariant1(b: Box1<String>) {}

fun test1() {
    acceptInvariant1(<!ARGUMENT_TYPE_MISMATCH!>out1<!>)
    acceptInvariant1(<!ARGUMENT_TYPE_MISMATCH!>in1<!>)
    acceptInvariant1(<!ARGUMENT_TYPE_MISMATCH!>star1<!>)
}

class Box2<T>(f: () -> T)
typealias OutBox2<T> = Box2<out T>
typealias InBox2<T> = Box2<in T>
typealias StarBox2 = Box2<*>

val out2 = OutBox2 { "" }
val in2 = InBox2 { "" } // K1: KT-60790
val star2 = StarBox2 { "" }

fun acceptInvariant2(b: Box2<String>) {}

fun test2() {
    acceptInvariant2(<!ARGUMENT_TYPE_MISMATCH!>out2<!>)
    acceptInvariant2(<!ARGUMENT_TYPE_MISMATCH!>in2<!>)
    acceptInvariant2(<!ARGUMENT_TYPE_MISMATCH!>star2<!>)
}

class Foo<T>(val x: Box1<out T>)
typealias TA<T> = Foo<T>
val foo = TA<CharSequence>(Box1(""))
