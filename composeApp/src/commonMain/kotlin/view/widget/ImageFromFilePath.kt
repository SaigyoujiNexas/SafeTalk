package view.widget

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import utils.ImageFromByteArray
import utils.byteArrayFromFile

@Composable
fun ImageFromFilePath(filePath: String, contentDescription: String) {
    if(filePath.isEmpty()) return
    val scope = rememberCoroutineScope()
    var byteArray: ByteArray by remember{ mutableStateOf(byteArrayOf()) }
    scope.launch {
        byteArray = byteArrayFromFile(filePath)
    }
    if(byteArray.isNotEmpty()) {
        val imageBitmap = ImageFromByteArray(byteArray, contentDescription)
        Image(imageBitmap, contentDescription)
    }
}