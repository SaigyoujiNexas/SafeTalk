package utils

import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

actual suspend fun byteArrayFromFile(filePath: String): ByteArray {
    return SystemFileSystem.source(Path( filePath)).buffered().readByteArray()
}