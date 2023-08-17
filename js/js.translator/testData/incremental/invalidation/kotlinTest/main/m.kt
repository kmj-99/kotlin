import common.*

fun box(stepId: Int) = when (stepId) {
        0 -> "OK"
        1 -> checkLog {
            suite("Test1") {
                test("foo") {
                    call("before")
                    call("foo")
                    call("after")
                }
            }
        }
        else -> "Fail: unexpected step $stepId"
    }
