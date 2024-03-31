package tab

import PaddingTab
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import client_api.CommunityService
import coil3.compose.AsyncImage
import entity.User
import home.ContentDetailScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.aroundScreenNavigator
import viewModel.CommunityModel

object HomeTab: PaddingTab, KoinComponent {
    @Composable
    override fun Content(paddingValues: PaddingValues) {
        val viewModel: CommunityModel = rememberScreenModel { CommunityModel() }
        val contents by viewModel.communityContents.collectAsState(emptyList())
        Column {
            LazyColumn {
                items(contents){
                    Card(modifier =  Modifier.fillMaxWidth().padding(12.dp), onClick = {
                        ContentDetailScreen.cid = it.id
                        aroundScreenNavigator.push(ContentDetailScreen)
                    }) {
                        Row(modifier = Modifier.padding(top = 12.dp, start = 12.dp)) {
                            val user = it.user
                            AsyncImage(
                                model = user.avatar,
                                contentDescription = "",
                                modifier = Modifier.size(48.dp),
                            )
                            Column {
                                Text(text = user.username)
                                Text(
                                    text = it.date,
                                    fontSize = 12.sp
                                    )
                            }
                        }
                        Text(text = it.title, modifier = Modifier
                            .padding(start = 8.dp)
                            .padding(vertical = 8.dp)
                            .align(Alignment.Start)
                        )
                        if(it.solved) {
                            Image(Icons.Outlined.Done,  "",
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(bottom = 8.dp, end = 8.dp)
                                )
                        }
                        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {}){
                                Icon(Icons.Outlined.Star, "")
                            }
                            IconButton(onClick = {}){
                                Icon(Icons.Outlined.ThumbUp, "")
                            }
                            IconButton(onClick = {
                            }){
                                Icon(Icons.Outlined.Share, "")
                            }
                        }
                    }
                }
            }
        }
    }

    override val options: TabOptions
        @Composable
        get(){
            val title = "主页"
            val icon = rememberVectorPainter(Icons.Outlined.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}