import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab

interface PaddingTab: Tab {
    @Composable
    fun Content(paddingValues: PaddingValues)

    @Composable
    override fun Content(){
        Content(PaddingValues(0.dp))
    }
}