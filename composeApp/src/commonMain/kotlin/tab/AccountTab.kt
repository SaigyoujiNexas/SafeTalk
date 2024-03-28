package tab

import PaddingTab
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.russhwolf.settings.get
import settings.settings

object AccountTab: PaddingTab {
    @Composable
    override fun Content(paddingValues: PaddingValues) {
        Column {
            Button(onClick = {
//                settings["token"] = null
                settings.remove("token")
            }){
                Text("登出")
            }
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "账户"
            val icon = rememberVectorPainter(Icons.Outlined.AccountCircle)
            return TabOptions(
                index = 2u,
                title = title,
                icon = icon
            )
        }
}