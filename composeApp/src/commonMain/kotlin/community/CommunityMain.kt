package community

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.LocalNavigatorSaver
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabNavigator
import utils.aroundScreenNavigator
import viewModel.CommunityModel

class CommunityMain(private val paddingValues: PaddingValues): Screen {
    @Composable
    override fun Content() {
        Box(androidx.compose.ui.Modifier.padding(paddingValues).fillMaxSize()){
            Community(modifier = Modifier
                .fillMaxSize()
                .zIndex(0f))
            FloatingActionButton(
                onClick = {
                    aroundScreenNavigator.push(CommunityEdit())
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 24.dp)
                    .zIndex(1f)
            ){
                Icon(Icons.Outlined.Edit, "edit")
            }
        }
    }
    @Composable
    private fun Community(modifier: Modifier = Modifier, ){
        val viewModel : CommunityModel = rememberScreenModel { CommunityModel() }
        val contents by viewModel.communityContents.collectAsState(emptyList())
        Column {
            LazyColumn {
                items(contents){
                    println(it)
                    Card {
                        Text(it.title)
                    }
                }
            }
        }

    }

}