// IGNORE_FIR
// In K2 the symbol is `<local>`.Local

fun foo() {
    class Local {
    }
    fun b<caret>ar() = Local()
}
