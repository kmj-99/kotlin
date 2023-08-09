// IGNORE_FIR
// In K2 A is `<local>`.A

fun test() {
    class A
    @Denotable("A") A()
    @Denotable("kotlin.collections.List<A>") listOf(A())
    @Nondenotable("`<anonymous>`") object {}
    @Nondenotable("kotlin.collections.List<`<anonymous>`>") listOf(object {})
}
