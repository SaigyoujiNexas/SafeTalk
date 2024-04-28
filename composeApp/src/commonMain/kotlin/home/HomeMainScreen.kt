package home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import community.CommunityEdit
import community.CommunityInfoListView
import entity.community.CommunityInfo
import kotlinx.coroutines.launch
import utils.AppNavigator

import viewModel.CommunityModel

class HomeMainScreen(private val paddingValues: PaddingValues = PaddingValues(0.dp)): Screen {
    @Composable
    override fun Content() {
        val viewModel: CommunityModel = rememberScreenModel { CommunityModel() }
        val contents by viewModel.communityContents.collectAsState(emptyList())
        val coroutineScope = rememberCoroutineScope()
        val extendPanel = @Composable { content: CommunityInfo ->
            var isCollected by remember { mutableStateOf(content.isCollected) }
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    coroutineScope.launch {
                        if (isCollected) {
                            viewModel.removeCollection(content.id)
                                .onSuccess {
                                    isCollected = false
                                }.onFailure {

                                }
                        } else {
                            viewModel.addCollection(content.id)
                                .onSuccess {
                                    isCollected = true
                                }.onFailure {
                                    print(it.message)
                                    println("failed to add collection")
                                }
                        }
                    }
                }) {
                    Icon(Icons.Outlined.Star, "", tint = if (isCollected) Color.Yellow else LocalContentColor.current)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.ThumbUp, "")
                }
                IconButton(onClick = {
                }) {
                    Icon(Icons.Outlined.Share, "")
                }
            }
        }
        Box(Modifier.padding(paddingValues)) {
            CommunityInfoListView(contents, extendPanel = extendPanel)
            FloatingActionButton(
                onClick = {
                    AppNavigator.instance.push(CommunityEdit())
                },
                modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
            ){
                Icon(Icons.Outlined.Edit, null)
            }
        }
    }
}