/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:JvmName("ExecutorsCLI")

package org.jetbrains.kotlin.native.executors.cli

private data class Args(
    val target: String,
    val noop: Boolean,
    val timeout: String?,
    val executable: String,
    val args: List<String>,
) {
    companion object {
        fun parse(args: Array<String>): Args {
            var target: String? = null
            var noop: Boolean = false
            var timeout: String? = null
            for (index in args.indices) {
                val arg = args[index]
                when {
                    arg.startsWith("--target=") -> {
                        check(target == null) {
                            "at arg $index: target was already specified: $target"
                        }
                        target = arg.removePrefix("--target=")
                    }
                    arg == "--noop" -> {
                        check(!noop) {
                            "at arg $index: noop was already specified"
                        }
                        noop = true
                    }
                    arg == "--timeout=" -> {
                        check(timeout == null) {
                            "at arg $index: timeout was already specified: $timeout"
                        }
                        timeout = arg.removePrefix("--timeout=")
                    }
                    arg == "--" -> {
                        check(target != null) {
                            "at arg $index: target was unspecified"
                        }
                        check(index + 1 < args.size) {
                            "at arg $index: executable was unspecified"
                        }
                        val executable = args[index + 1]
                        val arguments = args.slice(index + 2 until args.size)
                        return Args(
                            target = target,
                            noop = noop,
                            timeout = timeout,
                            executable = executable,
                            args = arguments,
                        )
                    }
                }
            }
            error("expected executable after --")
        }
    }
}

fun main(args: Array<String>) {
    val args = Args.parse(args)
}