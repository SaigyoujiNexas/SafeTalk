package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class LoginRegisterModel: ScreenModel {
    var telOrEmail by mutableStateOf("")
    var password by mutableStateOf( "")
    var verifyCode by mutableStateOf("")
}