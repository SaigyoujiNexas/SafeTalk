import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import client_api.UserService
import com.russhwolf.settings.get
import di.netWorkModule
import entity.account.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import login_register.LoginRegisterScreen
import main.MainScreen
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import settings.settings

@Composable
fun App() {
    KoinApplication(application ={
        modules(netWorkModule)
    }
    ) {
        var loginStatus by remember { mutableStateOf(LoginStatus.None) }
        MaterialTheme{
            val token: String? = settings["token"]
            if(token.isNullOrEmpty()) {
                Navigator(LoginRegisterScreen())
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
                        Navigator(LoginRegisterScreen())
                    }
                    LoginStatus.None -> {

                    }
                    LoginStatus.Logining -> {

                    }
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