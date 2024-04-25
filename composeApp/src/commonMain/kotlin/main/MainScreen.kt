package main

import PaddingTab
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import tab.AccountTab
import tab.CommunityTab
import tab.HomeTab
import tab.MessageTab
import utils.aroundScreenNavigator

class MainScreen: Screen {
    @Composable
    override fun Content() {
        aroundScreenNavigator = LocalNavigator.currentOrThrow
        TabNavigator(HomeTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(CommunityTab)
                        TabNavigationItem(MessageTab)
                        TabNavigationItem(AccountTab)
                    }
                },
                content = {
                    currentTab(it)
                },
            )
        }
    }
}
@Composable
fun currentTab(paddingValues: PaddingValues){
    val tabNavigator = LocalTabNavigator.current
    val currentTab = tabNavigator.current as PaddingTab

    tabNavigator.saveableState("currentTab") {
        currentTab.Content(paddingValues)
    }
}
@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab, onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
    )
}