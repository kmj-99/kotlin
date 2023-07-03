
plugins {
    id("org.jetbrains.kotlin.test.fixes.android")

    kotlin("multiplatform")
}

val disambiguationAttribute = Attribute.of("disambiguationAttribute", String::class.java)

kotlin {
    jvm("plainJvm") {
        attributes { attribute(disambiguationAttribute, "plainJvm") }
    }

    jvm("jvmWithJava") {
        withJava()
        attributes { attribute(disambiguationAttribute, "jvmWithJava") }
    }
}
