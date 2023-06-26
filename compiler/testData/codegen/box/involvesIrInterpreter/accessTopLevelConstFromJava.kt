// TARGET_BACKEND: JVM
// FILE: Bar.java
package one.two;

public class Bar {
    public static final int BAR = MainKt.FOO + 1;
}

// FILE: Main.kt
package one.two

const val FOO = <!EVALUATED("1")!>1<!>

const val BAZ = Bar.BAR <!EVALUATED("3")!>+ 1<!>

fun box(): String {
    return "OK"
}
