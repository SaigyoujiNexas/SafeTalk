package user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import collection.CollectionListScreen
import entity.account.CurrentUser
import io.ktor.http.*
import login_register.LoginRegisterScreen
import settings.settings
import utils.AppNavigator

class AccountMain(private val paddingValues: PaddingValues): Screen{

    @Composable
    override fun Content() {
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(48.dp))
            AsyncImage(CurrentUser!!.avatar, "",
                modifier = Modifier.align(Alignment.CenterHorizontally).size(48.dp))
            Spacer(Modifier.height(12.dp))
            Text(CurrentUser!!.username)
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Text("我的收藏", modifier = Modifier.fillMaxWidth().clickable {
                AppNavigator.instance.push(CollectionListScreen(uid = CurrentUser!!.uid))
            }
                .padding(start = 12.dp, top = 12.dp, bottom = 12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))
            Text("我的发帖", modifier = Modifier.fillMaxWidth().padding(start = 12.dp),)
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.padding(12.dp))
            Button(onClick = {
                settings.remove("token")
                AppNavigator.instance.popAll()
                AppNavigator.instance.push(LoginRegisterScreen())
            }){
                    Text("登出")
            }
        }
    }
}
