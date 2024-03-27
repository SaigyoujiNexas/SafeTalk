package login_register

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

class LoginRegisterScreen: Screen {
    @Composable
    override fun Content() {
        Navigator(LoginRegisterMain())
    }
}