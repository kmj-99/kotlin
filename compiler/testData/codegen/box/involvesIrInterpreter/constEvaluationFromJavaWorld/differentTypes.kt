// TARGET_BACKEND: JVM
// FILE: Bar.java
public class Bar {
    public static final char CHAR = MainKt.CHAR + 1;
    public static final boolean BOOL = !MainKt.BOOL;
    public static final byte BYTE = MainKt.BYTE + 1;
    public static final short SHORT = MainKt.SHORT + 1;
    public static final int INT = MainKt.INT + 1;
    public static final long LONG = MainKt.LONG + 1L;
    public static final float FLOAT = MainKt.FLOAT + 1.0f;
    public static final double DOUBLE = MainKt.DOUBLE + 1.0;
    public static final String STRING = MainKt.STRING + "3";
}

// FILE: Main.kt
const val CHAR: Char = '1' <!EVALUATED("2")!>+ 1<!>
const val BOOL: Boolean = <!EVALUATED("true")!>true<!>
const val BYTE: Byte = (1.toByte() + 1).<!EVALUATED("2")!>toByte()<!>
const val SHORT: Short = (1.toShort() + 1).<!EVALUATED("2")!>toShort()<!>
const val INT: Int = 1 <!EVALUATED("2")!>+ 1<!>
const val LONG: Long = 1L <!EVALUATED("2")!>+ 1L<!>
const val FLOAT: Float = 1.5f <!EVALUATED("2.0")!>+ .5f<!>
const val DOUBLE: Double = 1.5 <!EVALUATED("2.0")!>+ 0.5<!>
const val STRING: String = "1" <!EVALUATED("12")!>+ "2"<!>

// FILE: usages.kt
const val CHAR_JAVA: Char = Bar.CHAR <!EVALUATED("4")!>+ 1<!>
const val BOOL_JAVA: Boolean = <!EVALUATED("true")!>!Bar.BOOL<!>
const val BYTE_JAVA: Byte = (Bar.BYTE + 1).<!EVALUATED("4")!>toByte()<!>
const val SHORT_JAVA: Short = (Bar.SHORT + 1).<!EVALUATED("4")!>toShort()<!>
const val INT_JAVA: Int = Bar.INT <!EVALUATED("4")!>+ 1<!>
const val LONG_JAVA: Long = Bar.LONG <!EVALUATED("4")!>+ 1L<!>
const val FLOAT_JAVA: Float = Bar.FLOAT <!EVALUATED("4.0")!>+ 1.0f<!>
const val DOUBLE_JAVA: Double = Bar.DOUBLE <!EVALUATED("4.0")!>+ 1.0<!>
const val STRING_JAVA: String = Bar.STRING <!EVALUATED("1234")!>+ "4"<!>

fun box(): String {
    return "OK"
}
