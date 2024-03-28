package viewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import client_api.CommunityService
import entity.community.CommunityContent
import entity.community.NewContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommunityEditScreenModel: ScreenModel, KoinComponent {

    var title by mutableStateOf("")
    var contents by mutableStateOf("")
    private val selectedImage = mutableListOf<ByteArray>()
    private var _images = MutableStateFlow<MutableList<ByteArray>>(mutableListOf())
    private val communityService :CommunityService by inject()
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
    suspend fun postContent(title: String, content: String, images: List<ByteArray>): Result<Unit>{
        val newContent = NewContent(title = title, content = content, images = images)
        val result = communityService.postContent(newContent)
        return result
    }
}