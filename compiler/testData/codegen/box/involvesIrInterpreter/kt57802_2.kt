// TARGET_BACKEND: JVM
// FILE: Bar.java
package one.two;

public class Bar {
    public static final int BAR = Foo.FOO + 1;
}

// FILE: Boo.java
package one.two;

public class Boo {
    public static final int BOO = Foo.BAZ + 1;
}

// FILE: Main.kt
package one.two

class Foo {
    companion object {
        const val FOO = <!EVALUATED("1")!>1<!>

        const val BAZ = Bar.BAR <!EVALUATED("3")!>+ 1<!>

        const val DOO = Boo.BOO <!EVALUATED("5")!>+ 1<!>
    }
}

fun box(): String {
    return "OK"
}
