import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import utils.AppNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    KoinApplication(application ={
        modules(netWorkModule)
    }
    ) {
        val stackDepth by AppNavigator.instance.stackCount.collectAsState()
        MaterialTheme{
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text("Safe Talk") },
                    navigationIcon = {
                        AnimatedVisibility(stackDepth != 0, enter = fadeIn() + expandHorizontally(), exit = fadeOut() + shrinkHorizontally()) {
                                IconButton(onClick = { AppNavigator.instance.pop() }) {
                                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "")
                                }
                        }
                    },
                    actions = {

                    }
                )
                Navigator(SplashScreen())
            }
        }
    }
}