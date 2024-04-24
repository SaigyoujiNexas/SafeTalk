package user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import login_register.LoginRegisterScreen
import settings.settings
import utils.aroundScreenNavigator

class AccountMain(private val paddingValues: PaddingValues): Screen{

    @Composable
    override fun Content() {
        Column(modifier = Modifier.padding(paddingValues)) {
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