import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import client_api.UserService
import com.russhwolf.settings.get
import entity.account.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import login_register.LoginRegisterScreen
import main.MainScreen
import org.koin.compose.koinInject
import settings.settings
import utils.AppNavigator

class SplashScreen(): Screen {
    @Composable
    override fun Content() {
        AppNavigator.instance.screenNavigator = LocalNavigator.currentOrThrow
        var loginStatus by remember { mutableStateOf(LoginStatus.None) }
        val token: String? = settings["token"]
        if(token.isNullOrEmpty()) {
            AppNavigator.instance.pop()
            AppNavigator.instance.push(LoginRegisterScreen())
        } else {
            if(loginStatus == LoginStatus.None)
                loginStatus = LoginStatus.Logining
            val userService: UserService = koinInject()
            rememberCoroutineScope().launch(Dispatchers.IO) {
                userService.getCurrentUserInfo(token).onSuccess {
                    CurrentUser = it
                    loginStatus = LoginStatus.LoginSuccess
                }.onFailure {
                    loginStatus = LoginStatus.LoginFailed
                }
            }
            when(loginStatus) {
                LoginStatus.LoginSuccess -> Navigator(MainScreen())
                LoginStatus.LoginFailed ->{
                    AppNavigator.instance.pop()
                    AppNavigator.instance.push(LoginRegisterScreen())
                }
                LoginStatus.None -> {

                }
                LoginStatus.Logining -> {

                }
            }
        }
    }
}
enum class LoginStatus{
    None,
    Logining,
    LoginSuccess,
    LoginFailed,
}
