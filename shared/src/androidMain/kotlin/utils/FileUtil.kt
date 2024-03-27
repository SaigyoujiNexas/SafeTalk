package utils

import BaseApplication
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

actual suspend fun byteArrayFromFile(filePath: String): ByteArray {
    if (filePath.isEmpty()) {
        throw IllegalArgumentException("File path cannot be empty")
    }
    val uri = Uri.parse(filePath)
    val stream = BaseApplication.instance.contentResolver
        .openInputStream(uri)
    return stream?.readBytes()?.also {
        stream.close()
    } ?: throw IllegalArgumentException("File not found")
}

fun assetFilePath(assetName: String): String {
    val context = BaseApplication.instance
    val file = File(context.filesDir, assetName)
    if (file.exists() && file.length() > 0) {
        return file.absolutePath
    }
    context.assets.open(assetName).use {input ->
        FileOutputStream(file).use {output->
            val buffer = ByteArray(4 * 1024)
            var read = 0;
            while(input.read(buffer).also { read = it } != -1){
               output.write(buffer, 0, read)
            }
            output.flush()
        }
    }
    return file.absolutePath
}