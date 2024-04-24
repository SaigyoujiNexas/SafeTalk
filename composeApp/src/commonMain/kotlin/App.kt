import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
        MaterialTheme{
            val token: String? = settings["token"]
            if(token.isNullOrEmpty()) {
                Navigator(LoginRegisterScreen())
            } else {
                val userService: UserService = koinInject()
                rememberCoroutineScope().launch(Dispatchers.IO) {
                    userService.getCurrentUserInfo(token).onSuccess {
                        CurrentUser = it
                    }
                }
                Navigator(MainScreen())
            }
        }
    }
}