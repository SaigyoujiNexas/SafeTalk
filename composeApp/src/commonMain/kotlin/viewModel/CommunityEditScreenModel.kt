package viewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import entity.community.CommunityContent
import entity.community.NewContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class CommunityEditScreenModel: ScreenModel {

    var title by mutableStateOf("")
    var contents by mutableStateOf("")
    private val selectedImage = mutableListOf<ByteArray>()
    private var _images = MutableStateFlow<MutableList<ByteArray>>(mutableListOf())
    val images: StateFlow<List<ByteArray>>
        get() = _images


    fun addImage(image: ByteArray){
        selectedImage.add(image)
        screenModelScope.launch {
            _images.emit(selectedImage)
        }
    }

    fun deleteImage(position: Int){
        selectedImage.removeAt(position)
        screenModelScope.launch {
            _images.emit(selectedImage)
        }
    }
    suspend fun postContent(title: String, content: String, images: List<ByteArray>): Result<Boolean>{
        val newContent = NewContent(title = title, content = content, images = images)
        return Result.success(true)
    }
}