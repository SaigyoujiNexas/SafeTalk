import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.russhwolf.settings.get
import di.netWorkModule
import login_register.LoginRegisterScreen
import main.MainScreen
import org.koin.compose.KoinApplication
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
                Navigator(MainScreen())
            }
        }
    }
}