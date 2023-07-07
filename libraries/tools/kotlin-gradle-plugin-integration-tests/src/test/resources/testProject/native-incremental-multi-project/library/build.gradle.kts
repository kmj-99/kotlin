plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    <SingleNativeTarget>("host")
//
//    sourceSets {
//        val commonMain by getting
//    }
}
