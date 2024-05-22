package com.gusd.autoinit

import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.util.Collections.emptyList

object ServicesFiles {
    const val INIT_MODULE_PATH = "META-INF/services"

    fun getPath(className: String): String {
        return "$INIT_MODULE_PATH/$className"
    }

    fun readServiceFile(input: InputStream): Set<String> {
        return input.bufferedReader(StandardCharsets.UTF_8).useLines { lines ->
            lines.filter { it.isNotBlank() && !it.startsWith("#") }.toSet()
        }
    }

    fun writeServiceFile(providers: Set<String>, outputStream: OutputStream) {
        val writer = outputStream.writer(StandardCharsets.UTF_8)
        writer.use { w ->
            providers.forEach {
                writer.write(it)
                writer.write("\n")
            }
            w.flush()
        }
    }
}