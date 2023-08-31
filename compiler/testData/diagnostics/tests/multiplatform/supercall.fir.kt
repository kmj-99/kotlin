// KT-61572: SUPER_CALL_WITH_DEFAULT_PARAMETERS must be raised for K2/Native in fun bar1()
// See also related test: compiler/testData/diagnostics/nativeTests/mppSupercallDefaultArguments.kt
// !LANGUAGE: +MultiPlatformProjects
// MODULE: common
// FILE: common.kt

package foo
expect open class A {
    open fun foo(x: Int = 20, y: Int = 3): Int
}

// MODULE: main()()(common)
// FILE: jvm.kt
package foo
actual open class A {
    actual open fun foo(x: Int, y: Int) = x + y
}

open class B : A() {
    override fun foo(x: Int, y: Int) = 0

    fun bar1() = super.foo()
}

fun box(): String {
    val v1 = B().bar1()
    if (v1 != 23) return "fail1: $v1"
    return "OK"
}
