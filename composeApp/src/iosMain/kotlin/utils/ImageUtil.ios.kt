package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

@Composable
actual fun ImageFromByteArray(
    byteArray: ByteArray,
    contentDescription: String
): ImageBitmap {
    return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}