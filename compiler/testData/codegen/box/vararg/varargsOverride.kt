// TARGET_BACKEND: JVM
// IGNORE_CODEGEN_WITH_IR_FAKE_OVERRIDE_GENERATION: KT-61370
// MODULE: lib
// FILE: A.java

public abstract class A<T> {
    protected abstract String doIt(T... args);

    public String test(T... args) {
        return doIt(args);
    }
}

// MODULE: main(lib)
// FILE: 1.kt

val a: A<Void> =
    object : A<Void>() {
        override fun doIt(vararg parameters: Void): String = "OK"
    }

fun box(): String = a.test()
