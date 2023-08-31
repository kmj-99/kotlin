/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.statistics.fileloggers

import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.lang.StringBuilder
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime

class FileRecordLogger(private val statisticsFolder: File, private val fileName: String) : IRecordLogger {
    private val profileFileNameSuffix = ".profile"
    private val outputStream = StringBuilder()

    override fun append(s: String) {
        outputStream.append("$s\n".toByteArray(MetricsContainer.ENCODING))
    }

    override fun close() {
        try {
            statisticsFolder.mkdirs()
            var file = File(statisticsFolder, fileName + profileFileNameSuffix)
            var suffixIndex = 0
            while (!file.createNewFile()) {
                file = File(statisticsFolder, "${fileName}.${suffixIndex++}$profileFileNameSuffix")
            }
        } catch (e: IOException) {
            NullRecordLogger()
        }
    }
}
