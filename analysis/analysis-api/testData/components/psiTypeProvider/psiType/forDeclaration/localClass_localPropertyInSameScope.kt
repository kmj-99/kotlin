// IGNORE_FIR
// In K2 the symbol is `<local>`.Local

fun foo() = run {
    class Local {
        fun bar(): Local {
            return this
        }
    }
    val p<caret> = Local().bar()
}
