package utils

import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.sync.Mutex
import kotlin.jvm.Synchronized

class AppNavigator(){

    lateinit var screenNavigator: Navigator
    private var currentStackDepth = 0
    private val _stackCount = MutableStateFlow(0)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    val stackCount: StateFlow<Int>
        get() = _stackCount


    fun push(screen: Screen){
        coroutineScope.launch{
            _stackCount.emit(++currentStackDepth)
        }
        screenNavigator.push(screen)
    }

    fun pop(){
        coroutineScope.launch {
            --currentStackDepth;
            if(currentStackDepth < 0) currentStackDepth = 0;
            _stackCount.emit(currentStackDepth)
        }
        screenNavigator.pop()
    }
    fun popAll(){
        coroutineScope.launch {
            currentStackDepth = 0
            _stackCount.emit(0)
        }
        screenNavigator.popAll()
    }
    companion object{
        val instance: AppNavigator by lazy {
            AppNavigator()
        }
    }
}