// TARGET_BACKEND: JVM

// WITH_REFLECT

class Eq {
    override fun equals(other: Any?): Boolean = true
    override fun toString(): String = "OK"
}

fun box(): String = Eq()::toString.callBy(mapOf())
