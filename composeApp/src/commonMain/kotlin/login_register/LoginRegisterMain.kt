package login_register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import utils.AppNavigator

class LoginRegisterMain : Screen {
    @Composable
    override fun Content() {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()){
            Text("欢迎使用")
            Spacer(Modifier.height(144.dp))
            Row {
                Button( onClick = {
                    AppNavigator.instance.push(LoginFirstStep())
                }){
                    Text("登录")
                }
                Spacer(Modifier.width(30.dp))
                Button(onClick = {

                }){
                    Text("注册")
                }
            }
        }
    }
}