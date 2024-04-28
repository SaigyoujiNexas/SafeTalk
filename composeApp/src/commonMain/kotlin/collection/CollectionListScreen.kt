package collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import community.CommunityInfoListView
import entity.LoadingStatus
import entity.account.CurrentUser
import entity.collection.CollectionInfo
import entity.community.CommunityInfo
import kotlinx.coroutines.launch
import org.koin.core.component.get
import repository.CollectionRepository

class CollectionListScreen(
    val paddingValues: PaddingValues = PaddingValues(0.dp),
    val uid: Int) : Screen {
    @Composable
    override fun Content() {
        val collectionRepo = CollectionRepository
        var loadingStatus: LoadingStatus<List<CollectionInfo>> by remember { mutableStateOf(LoadingStatus.PreStartLoading) }
        val coroutineContext = rememberCoroutineScope()
        if(loadingStatus == LoadingStatus.PreStartLoading) {
            coroutineContext.launch {
                loadingStatus = LoadingStatus.Loading
                val collections = if (uid == CurrentUser!!.uid) {
                    val data = collectionRepo.getMyCollection()
                    if (data.isSuccess) {
                        loadingStatus = LoadingStatus.LoadingSuccess(data.getOrNull()!!)
                    } else {
                        loadingStatus = LoadingStatus.LoadingFailed(data.exceptionOrNull()!!, data.exceptionOrNull()!!.message?:data.exceptionOrNull()!!.stackTraceToString())
                    }
                } else {
                    TODO("load other account collections")
                }
            }
        }
        when(loadingStatus){
            LoadingStatus.PreStartLoading, LoadingStatus.Loading -> {
                CircularProgressIndicator()
            }
            is LoadingStatus.LoadingSuccess<List<CollectionInfo>> -> {
                val contents: List<CollectionInfo> = (loadingStatus as LoadingStatus.LoadingSuccess<List<CollectionInfo>>).data
                val communityInfo = contents.map { it.contentInfo }
                CommunityInfoListView(communityInfo, modifier = Modifier.padding(paddingValues))
            }
            is LoadingStatus.LoadingFailed ->{
                Box(contentAlignment = Alignment.Center){
                    Text((loadingStatus as LoadingStatus.LoadingFailed).reason)
                }
            }
        }
    }
}