package tab

import PaddingTab
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.TabOptions

object HomeTab: PaddingTab {
    @Composable
    override fun Content(paddingValues: PaddingValues) {
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