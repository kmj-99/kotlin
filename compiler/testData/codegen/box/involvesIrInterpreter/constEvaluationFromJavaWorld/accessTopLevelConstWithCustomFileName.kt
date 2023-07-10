// TARGET_BACKEND: JVM
// IGNORE_LIGHT_ANALYSIS
// WITH_STDLIB
// FILE: Bar.java
package one.two;

public class Bar {
    public static final int BAR = OtherKt.FOO + 1;
}

// FILE: Main.kt
@file:JvmName(<!EVALUATED("OtherKt")!>"OtherKt"<!>)
package one.two

const val FOO = <!EVALUATED("1")!>1<!>

const val BAZ = Bar.BAR <!EVALUATED("3")!>+ 1<!>

fun box(): String {
    return "OK"
}
