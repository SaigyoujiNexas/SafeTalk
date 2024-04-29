package home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import entity.community.Comment
import entity.community.CommunityDetail
import entity.community.NewComment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.byteArrayFromFile
import viewModel.CommunityModel

object ContentDetailScreen: Screen {
    var cid by mutableStateOf(-1)

    @Composable
    override fun Content() {
        val communityModel = rememberScreenModel { CommunityModel() }
        communityModel.setCid(cid)
        val scope = rememberCoroutineScope()
        val contentDetail by communityModel.currentCommunityContent.collectAsState(null)
        var showSendSuccessDialog by remember { mutableStateOf(false) }
        if (contentDetail == null) {
            return
        } else {
            Scaffold(
                bottomBar = {
                    BottomAppBar() {
                        CommentEdit(
                            communityContent = contentDetail!!,
                            onSendComment = {
                                communityModel.sendComment(it, onSendSuccess = {
                                    showSendSuccessDialog = true
                                    scope.launch {
                                        delay(1000)
                                        showSendSuccessDialog = false
                                        delay(200)

                                    }
                                })
                            }
                        )
                    }
                }

            ) {
                ContentDetail(contentDetail!!, modifier = Modifier.padding(it))
            }
        }
        AnimatedVisibility(visible = showSendSuccessDialog) {
            Dialog(
                onDismissRequest = {
                    showSendSuccessDialog = false
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false
                )
            ) {
                Card(modifier = Modifier.width(128.dp)) {
                    Icon(Icons.Outlined.Done, null, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(72.dp)
                        .padding(top = 24.dp)
                        )
                    Text("发送成功", modifier = Modifier.padding(bottom = 24.dp)
                        .align(Alignment.CenterHorizontally))
                }
            }
        }
    }

    @Composable
    fun ContentDetail(contentDetail: CommunityDetail, modifier: Modifier = Modifier) {
        val user = contentDetail.user
        val columns = GridCells.Adaptive(120.dp)
        LazyVerticalGrid(
            columns = columns,
            modifier = modifier
                .padding(horizontal = 24.dp)
        ) {

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                Column {
                    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        AsyncImage(user.avatar, contentDescription = null, modifier =
                        Modifier.size(48.dp).clickable {
                            }.align(Alignment.CenterVertically)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween) {
                            Text(user.username, style = MaterialTheme.typography.bodyMedium)
                            Text(contentDetail.date, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                        Spacer(Modifier.height(12.dp))
                        Text(contentDetail.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(12.dp))
                        Text(contentDetail.content)
                }
            }
            items(contentDetail.images){
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)){
                    AsyncImage(it, null, modifier = Modifier.align(Alignment.Center))
                }
            }
//            item(span = {
//                GridItemSpan(maxLineSpan)
//            }) {
//                Column(modifier.fillMaxWidth()) {
//                    HorizontalDivider()
//                }
//            }
            if (contentDetail.comments.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier.fillMaxWidth()) {
                        Text(
                            text = "还木有评论",
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            } else {
                items(contentDetail.comments.size, span = { GridItemSpan(maxLineSpan) }) {
                    Comment(contentDetail.comments[it], modifier = Modifier.fillMaxWidth())
                    HorizontalDivider()
                }
            }
        }
    }

    @Composable
    fun Comment(comment: Comment, modifier: Modifier = Modifier) {
        Column(modifier = modifier.padding(vertical = 12.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                Row {
                    AsyncImage(
                        comment.user.avatar, null, modifier = Modifier.size(48.dp).align(Alignment.CenterVertically)
                    )
                    Column(modifier = Modifier.padding(start = 12.dp).fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween) {
                        Text(comment.user.username, style = MaterialTheme.typography.bodyMedium)
                        Text(comment.date, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Column(modifier = Modifier.padding(start = 60.dp, top = 12.dp)) {
                Text(comment.content, style = MaterialTheme.typography.bodyMedium)
                LazyRow() {
                    items(comment.images.size) {
                        AsyncImage(comment.images[it], null)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun CommentEdit(
        modifier: Modifier = Modifier,
        communityContent: CommunityDetail,
        onSendComment: (NewComment) -> Unit = {},
        onAddImage: (ByteArray) -> Unit = {}
    ) {
        val scope = rememberCoroutineScope()
        var expanded by remember { mutableStateOf(false) }
        var images: List<ByteArray> by remember { mutableStateOf(emptyList()) }
        var comment by remember { mutableStateOf("") }
        var showFilePicker by remember { mutableStateOf(false) }
        if (!expanded) {
            Row(modifier = modifier.fillMaxWidth()) {
                Text("发一条评论吧", modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = true
                    }
                    .padding(12.dp)
                    .background(Color.Gray.copy(alpha = 0.6f), shape = RoundedCornerShape(100.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                    color = Color.Black
                )
            }
        } else {
            Column(modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            showFilePicker = true
                        },
                        modifier = Modifier
                            .padding(end = 12.dp)
                    ) {
                        Icon(
                            painterResource(DrawableResource("icon_image.webp")),
                            null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    TextField(
                        value = comment,
                        onValueChange = { comment = it },
                        modifier = Modifier
                            .weight(1f)
                    )
                    IconButton(onClick = {
                        onSendComment(
                            NewComment(
                                content = comment,
                                images = images,
                                communityId = communityContent.id
                            )
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Outlined.Send, null)
                    }
                }

            }
            FilePicker(show = showFilePicker, fileExtensions = listOf("jpg", "png", "webp", "jpeg")) { file ->
                showFilePicker = false;
                scope.launch {
                    val byte = byteArrayFromFile(file?.path ?: return@launch)
                    images = images.toMutableList().also { it.add(byte) }
                    onAddImage(byte)
                }
            }
        }
    }
}