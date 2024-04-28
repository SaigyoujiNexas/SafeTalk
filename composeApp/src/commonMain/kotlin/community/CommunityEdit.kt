package community

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.AppNavigator
import utils.ImageFromByteArray
import utils.byteArrayFromFile
import viewModel.CommunityEditScreenModel

class CommunityEdit : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val communityEditScreenModel: CommunityEditScreenModel = rememberScreenModel { CommunityEditScreenModel() }
        val selectedImages by communityEditScreenModel.images.collectAsState(emptyList())
        var showSendSuccessDialog by remember { mutableStateOf(false) }
        var showProgressDialog by remember { mutableStateOf(false) }
        val filetype = listOf("jpg", "png", "webp", "jpeg")
        var showFilePicker by remember {
            mutableStateOf(false)
        }
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = {
                            showFilePicker = true
                        }) {
                            Icon(
                                painterResource(DrawableResource("icon_image.webp")),
                                "add",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                scope.launch(Dispatchers.IO) {
                                    communityEditScreenModel.postContent(
                                        title = communityEditScreenModel.title,
                                        content = communityEditScreenModel.contents,
                                        images = selectedImages,
                                        onContentUpLoadSucceed = {
                                            scope.launch {
                                                showSendSuccessDialog = true
                                                delay(1000)
                                                showSendSuccessDialog = false
                                                delay(300)
                                                AppNavigator.instance.pop()
                                            }
                                        }
                                    )
                                }

                            },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.Send, "send")
                        }
                    }
                )
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Editor(
                    communityEditScreenModel = communityEditScreenModel,
                    selectedImages = selectedImages,
                )
                AnimatedVisibility(visible = showSendSuccessDialog){
                    Dialog(
                        onDismissRequest = {
                            AppNavigator.instance.pop()
                        }, properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        )
                    ) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(240.dp)
                                .padding(horizontal = 48.dp, vertical = 24.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Done, null, modifier = Modifier
                                    .size(48.dp).align(Alignment.CenterHorizontally)
                            )
                            Text("发送成功", modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
                AnimatedVisibility(visible = showProgressDialog){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
        FilePicker(show = showFilePicker, fileExtensions = filetype) { file ->
            showFilePicker = false
            scope.launch {
                communityEditScreenModel.addImage(byteArrayFromFile(file?.path ?: return@launch))
            }
        }
    }

    @Composable
    fun Editor(
        communityEditScreenModel: CommunityEditScreenModel = rememberScreenModel { CommunityEditScreenModel() },
        selectedImages: List<ByteArray> = emptyList(),
        modifier: Modifier = Modifier
    ) {


        val scope = rememberCoroutineScope()

        Box(
            modifier = modifier.fillMaxSize()
        ) {
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