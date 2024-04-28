package community

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import entity.community.CommunityInfo
import home.ContentDetailScreen
import utils.AppNavigator

@Composable
fun CommunityInfoListView(
    contents: List<CommunityInfo>,
    extendPanel: @Composable (content: CommunityInfo) -> Unit = {},
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier) {
        items(contents) {
            Card(modifier = Modifier.fillMaxWidth().padding(12.dp), onClick = {
                ContentDetailScreen.cid = it.id
                AppNavigator.instance.push(ContentDetailScreen)
            }) {
                Row(modifier = Modifier.padding(top = 12.dp, start = 12.dp)) {
                    AsyncImage(
                        model = it.userAvatar,
                        contentDescription = "",
                        modifier = Modifier.size(48.dp),
                    )
                    Column {
                        Text(text = it.userName)
                        Text(
                            text = it.time,
                            fontSize = 12.sp
                        )
                    }
                }
                Text(
                    text = it.title, modifier = Modifier
                        .padding(start = 8.dp)
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                )
                if (it.solved) {
                    Image(
                        Icons.Outlined.Done, "",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(bottom = 8.dp, end = 8.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
                extendPanel(it)
            }
        }
    }
}