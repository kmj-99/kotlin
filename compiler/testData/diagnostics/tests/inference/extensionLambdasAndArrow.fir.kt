// !DIAGNOSTICS: -UNUSED_VARIABLE -UNUSED_ANONYMOUS_PARAMETER

fun <T> select(vararg x: T) = x[0]

fun main() {
    val x1: String.() -> String = if (true) {{ this }} else {{ this }}
    val x2: String.() -> String = if (true) {{ -> this }} else {{ -> this }}
    val x3: () -> String = if (true) {{ -> "this" }} else {{ -> "this" }}
    val x4: String.() -> String = <!TYPE_MISMATCH!>if (true) {{ str: String -> "this" }} else {{ str: String -> "this" }}<!>
    val x41: String.(String) -> String = <!TYPE_MISMATCH!>if (true) {{ str: String, str2: String -> "this" }} else {{ str: String, str2: String -> "this" }}<!>
    val x42: String.(String) -> String = <!TYPE_MISMATCH, TYPE_MISMATCH!>if (true) {{ str, <!CANNOT_INFER_PARAMETER_TYPE!>str2<!> -> "this" }} else {{ str, <!CANNOT_INFER_PARAMETER_TYPE!>str2<!> -> "this" }}<!>
    val x5: String.() -> String = <!TYPE_MISMATCH, TYPE_MISMATCH!>if (true) {{ <!CANNOT_INFER_PARAMETER_TYPE!>str<!> -> "this" }} else {{ <!CANNOT_INFER_PARAMETER_TYPE!>str<!> -> "this" }}<!>
    val x6: String.() -> String = <!TYPE_MISMATCH!>if (true) {{ <!CANNOT_INFER_PARAMETER_TYPE!>str<!> -> "this" }} else {{ "this" }}<!>
    val x7: String.() -> String = select({ -> this }, { -> this })
    val x8: String.() -> String = select({ this }, { this })
}
