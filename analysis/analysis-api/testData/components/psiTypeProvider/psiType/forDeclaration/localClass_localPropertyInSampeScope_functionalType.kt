// IGNORE_FIR
// In K2 the symbol is `<local>`.Local

fun foo() {
    class Local {
    }
    val a<caret> = fun (): Local {
        return Local()
    }
}
