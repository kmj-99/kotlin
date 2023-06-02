// IGNORE_BACKEND_K2: JVM_IR

enum class Build { Debug, Release }

fun applySomething(build: Build) = when (build) {
    Build.Debug -> "OK"
    Build.Release -> "fail"
}

val rv = applySomething(Build.Debug)

// expected: rv: OK