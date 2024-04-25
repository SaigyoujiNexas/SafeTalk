package user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import entity.account.CurrentUser
import login_register.LoginRegisterScreen
import settings.settings
import utils.aroundScreenNavigator

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
            Button(onClick = {
                settings.remove("token")
                aroundScreenNavigator.popAll()
                aroundScreenNavigator.push(LoginRegisterScreen())
            }){
                    Text("登出")
            }
        }
    }
}