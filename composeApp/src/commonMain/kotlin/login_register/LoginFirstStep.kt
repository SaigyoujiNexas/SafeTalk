package login_register

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import client_api.LoginService
import com.russhwolf.settings.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main.MainScreen
import org.koin.compose.koinInject
import tab.CommunityTab
import utils.isEmail
import utils.isTel
import viewModel.LoginRegisterModel

class LoginFirstStep : Screen {
    @Composable
    override fun Content() {
        val loginService =  koinInject<LoginService>()
        val loginModel = rememberScreenModel { LoginRegisterModel() }
        var loginStatus by remember{ mutableStateOf(0) }
        val scope = rememberCoroutineScope()
        var token by remember { mutableStateOf("") }
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch(Dispatchers.IO){
                            val isTel = loginModel.telOrEmail.isTel()
                            token = loginService.login(
                                tel = if(isTel) loginModel.telOrEmail else "",
                                email = if(loginModel.telOrEmail.isEmail()) loginModel.telOrEmail else "",
                                verifyCode = loginModel.verifyCode,
                                password = loginModel.password
                            )
                            settings.settings["token"] = token
                            if(token.isNotEmpty()){
                                navigator.replaceAll(MainScreen())
                            }
                        }
                    },
                ) {
                    Icon(Icons.Outlined.ArrowForward, "")
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = loginModel.telOrEmail,
                        onValueChange = { loginModel.telOrEmail = it },
                        label = { Text("请输入手机号或邮箱") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (loginStatus == 0) {
                        PasswordInput(loginModel)
                    } else {
                        VerifyCodeInput(loginModel)
                    }
                    Text(if (loginStatus == 0) "验证码登录" else "密码登录" ,
                        modifier = Modifier.clickable {
                            loginStatus = if (loginStatus == 0) 1 else 0
                        }.align(Alignment.Start)
                    )
                    Text(token)
                }
            }
        }
    }
    @Composable
    private fun PasswordInput(
        loginModel: LoginRegisterModel = rememberScreenModel { LoginRegisterModel() }
        , modifier: Modifier = Modifier){
        OutlinedTextField(
            value = loginModel.password,
            onValueChange = { loginModel.password = it },
            label = { Text("请输入密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier=  modifier.fillMaxWidth()
        )
    }

    @Composable
    private fun VerifyCodeInput(
        loginModel: LoginRegisterModel = rememberScreenModel { LoginRegisterModel() },
        modifier: Modifier = Modifier){
        val scope = rememberCoroutineScope()
        var countdown by remember {
            mutableStateOf(0)
        }
        var clickable  by remember { mutableStateOf(true) }
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(
                value = loginModel.verifyCode,
                onValueChange = { loginModel.verifyCode = it },
                label = { Text("请输入验证码") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            Button(onClick = {
                scope.launch {
                    var allTime = 60 * 1000;
                    countdown = allTime / 1000;
                    clickable = false
                    while(allTime > 0) {
                        delay(1000)
                        allTime -= 1000
                        countdown = allTime / 1000;
                    }
                    clickable = true
                }
            }, enabled = clickable,
                modifier = Modifier.animateContentSize()
            ) {
                Text(if(clickable) "获取验证码" else "$countdown")
            }
        }
    }
    fun convertImage(image: ImageBitmap){
        image
    }
}