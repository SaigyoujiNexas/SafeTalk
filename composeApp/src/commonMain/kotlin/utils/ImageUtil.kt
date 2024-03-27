package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asSkiaBitmap
//import org.jetbrains.skia.EncodedImageFormat
//import org.jetbrains.skia.Image
//
//@Composable
//fun ImageBitmap.toBytes(): ByteArray {
//
//    return Image.makeFromBitmap(this.asSkiaBitmap())
//        .encodeToData(EncodedImageFormat.JPEG, 100)
//        ?.bytes?:throw Exception("Failed to convert ImageBitmap to ByteArray")
//}
//@Composable
//fun test(image: ImageBitmap){
//    image.asSkiaBitmap()
//    image.toBytes()
//}
@Composable
expect fun ImageFromByteArray(byteArray: ByteArray, contentDescription: String) : ImageBitmap