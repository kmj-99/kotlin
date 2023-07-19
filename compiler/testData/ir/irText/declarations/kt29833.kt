// SKIP_SIGNATURE_DUMP
// ^ K2 will not enhance types for constants from Java, K1 does
// TARGET_BACKEND: JVM
// FILE: Definitions.kt
// IR_FILE: kt29833.txt
package interop

object Definitions {
    const val KT_CONSTANT = Interface.CONSTANT

    val ktValue = Interface.CONSTANT
}

// FILE: interop/Interface.java
package interop;

class Interface {
    public static final String CONSTANT = "constant";
    public CharSequence chars;
}
