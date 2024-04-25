package tab

import PaddingTab
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import entity.account.CurrentUser
import org.koin.core.component.KoinComponent

object MessageTab: PaddingTab, KoinComponent {
    @Composable
    override fun Content(paddingValues: PaddingValues) {
        Column(Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Card(Modifier.fillMaxWidth().padding(8.dp)) {
                Row {
                    Column {
                        AsyncImage(CurrentUser!!.avatar, "", modifier = Modifier.size(48.dp))
                        Text("silent碎月")
                    }
                    Text("我觉得是个好主意")
                }
            }
            Spacer(Modifier.height(12.dp))
            Card(Modifier.fillMaxWidth().padding(8.dp)) {
                Row {
                    Column {
                        AsyncImage("https://i0.hdslb.com/bfs/face/ef5008d1b7b66b9884250d313da7915488874fe1.jpg@150w_150h.jpg", "", modifier = Modifier.size(48.dp))
                        Text("摸个鱼先")
                    }
                    Text("我觉得是个好主意")
                }
            }
        }
    }

    override val options: TabOptions
        @Composable
        get(){
            val title = "私信"
            val icon = rememberVectorPainter(Icons.Outlined.MailOutline)
            return remember {
                TabOptions(
                    title = title,
                    icon = icon,
                    index = 2u,
                )
            }
        }

}