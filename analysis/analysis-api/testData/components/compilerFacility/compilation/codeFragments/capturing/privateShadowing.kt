open class A {
    private fun foo() = 2
}

class B: A() {
    private fun foo() = 3
}
fun test(b: B) {
    <caret>val x = 0
}
