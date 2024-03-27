package tab

import PaddingTab
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object AccountTab: PaddingTab {
    @Composable
    override fun Content(paddingValues: PaddingValues) {
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