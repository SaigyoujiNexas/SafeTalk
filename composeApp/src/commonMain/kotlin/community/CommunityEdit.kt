package community

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.ImageFromByteArray
import utils.byteArrayFromFile
import utils.checkImage
import viewModel.CommunityEditScreenModel

class CommunityEdit : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        var showFilePicker by remember {
            mutableStateOf(false)
        }
        val communityEditScreenModel: CommunityEditScreenModel = rememberScreenModel { CommunityEditScreenModel() }
        val filetype = listOf("jpg", "png", "webp")
        val scope = rememberCoroutineScope()
        var score by remember { mutableStateOf("") }
        val selectedImages by communityEditScreenModel.images.collectAsState(emptyList())
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = {
                            showFilePicker = true
                        }) {
                            Icon(
                                painterResource("icon_image.webp"),
                                "add",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                scope.launch(Dispatchers.Default) {
                                    val imageLevel = checkImage(selectedImages[0])
                                    score = imageLevel.name
                                }

                            },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Outlined.Send, "send")
                        }
                    }
                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues).fillMaxSize()
            ) {
                FilePicker(show = showFilePicker, fileExtensions = filetype) { file ->
                    showFilePicker = false;
                    scope.launch {
                        communityEditScreenModel.addImage(byteArrayFromFile(file?.path ?: return@launch))
                    }
                }
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(
                        value = communityEditScreenModel.title,
                        onValueChange = {
                            communityEditScreenModel.title = it
                        },
                        maxLines = 1,
                        label = { Text("标题") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = communityEditScreenModel.contents,
                        onValueChange = {
                            communityEditScreenModel.contents = it
                        },
                        label = { Text("正文") },
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f)
                    )
                    Text(score)
                }
                LazyRow(
                    modifier = Modifier.align(Alignment.BottomStart)
                        .padding(start = 12.dp, bottom = 12.dp)
                ) {
                    selectedImages.forEachIndexed { index, item ->
                        item {
                            val imageBitmap = ImageFromByteArray(item, "image$index")
                            Box(Modifier) {
                                Image(
                                    imageBitmap,
                                    "image$index",
                                    modifier = Modifier.width(48.dp),
                                )
                                IconButton(onClick = {
                                    communityEditScreenModel.deleteImage(index)
                                }, modifier = Modifier.align(Alignment.Center)) {
                                    Icon(Icons.Rounded.Delete, "delete")
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    }

}