import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun main() =
        runBlocking {
            application {
            Window(onCloseRequest = ::exitApplication, title = "SafeTalk") {
            App()
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}