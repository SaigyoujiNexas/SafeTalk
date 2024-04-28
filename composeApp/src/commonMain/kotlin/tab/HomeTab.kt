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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import home.ContentDetailScreen
import home.HomeMainScreen
import org.koin.core.component.KoinComponent
import viewModel.CommunityModel

object HomeTab: PaddingTab, KoinComponent {
    @Composable
    override fun Content(paddingValues: PaddingValues) {
        HomeMainScreen(paddingValues).Content()
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