fun <K> foo(x: K) {}
val x = foo<(<!UNRESOLVED_REFERENCE!>unresolved<!>) -> Float> { <!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>it<!>.<!DEBUG_INFO_MISSING_UNRESOLVED!>toFloat<!>() }
