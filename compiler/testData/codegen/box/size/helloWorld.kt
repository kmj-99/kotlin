// TARGET_BACKEND: WASM

// WASM_DCE_EXPECTED_OUTPUT_SIZE: wasm 44_788
// WASM_DCE_EXPECTED_OUTPUT_SIZE:  mjs  5_516

fun box(): String {
    println("Hello, World!")
    return "OK"
}
