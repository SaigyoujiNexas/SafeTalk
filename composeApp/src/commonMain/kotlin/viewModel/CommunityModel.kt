package viewModel

import androidx.compose.ui.text.resolveDefaults
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import client_api.CollectionService
import client_api.CommunityService
import entity.account.CurrentUser
import entity.community.CommunityDetail
import entity.community.CommunityInfo
import entity.community.NewComment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommunityModel: ScreenModel, KoinComponent {
    var searchKeyWords = ""

    private val _communityContents = MutableStateFlow(emptyList<CommunityInfo>())
    private val communityService: CommunityService by inject()
    private val collectionService: CollectionService by inject()

    private val _currentCommunityContent: MutableStateFlow<CommunityDetail?> = MutableStateFlow(null)
    val communityContents: Flow<List<CommunityInfo>>
        get() = _communityContents

    val currentCommunityContent: StateFlow<CommunityDetail?>
        get() = _currentCommunityContent
    init {
        getAllCommunityContent()
    }
    fun setCid(cid: Int) = screenModelScope.launch {
        if(cid == -1) return@launch
        communityService.getContentDetail(cid).onSuccess {
            _currentCommunityContent.emit(it)
        }
    }
    private fun getAllCommunityContent() = screenModelScope.launch {
        val res = communityService.getAllContent()
        if(res.isSuccess){
            _communityContents.emit(res.getOrElse { emptyList() })
        }
    }

    fun sendComment(comment: NewComment, onSendSuccess: () -> Unit = {}) = screenModelScope.launch{
        val res = communityService.sendComment(comment)
        if(res.isSuccess){
            onSendSuccess()
        }
    }
    suspend fun addCollection(communityContentId: Int): Result<Unit>{
        val res = collectionService.addCollection(communityContentId)
        return res
    }
    suspend fun removeCollection(communityContentId: Int): Result<Unit>{
        val res = collectionService.removeCollection(communityContentId)
        return res
    }

}