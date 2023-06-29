plugins {
    id("kotlin")
}

description = "Contains test for the compose compiler daemon"

kotlin.jvmToolchain(11)

dependencies {
    testImplementation(project(":kotlin-stdlib"))
    testImplementation(project(":plugins:compose-compiler-plugin:compiler-daemon"))
    testImplementation(project(":compiler:cli-common"))

    testImplementation(commonDependency("junit:junit"))

    testRuntimeOnly(projectTests(":compiler:tests-common"))
    testRuntimeOnly(intellijCore())
}

projectTest(parallel = true) {
    dependsOn(":dist")
    workingDir = rootDir
}

