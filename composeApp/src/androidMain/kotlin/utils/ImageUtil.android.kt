package utils

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
actual fun ImageFromByteArray(
    byteArray: ByteArray,
    contentDescription: String
): ImageBitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        .asImageBitmap()
}