package org.example.project.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object VerifyUtil {
private val verifyMap = mutableMapOf<String, Pair<String, Long>>()


    fun addVerifyCode(key: String, code: String) {
        verifyMap[key] = Pair(code, System.currentTimeMillis())
        tryCleanVerifyCode()
    }
    fun checkVerifyCode(key: String, code: String): Boolean {
        val pair = verifyMap[key] ?: return false
        tryCleanVerifyCode()
        return pair.first == code
    }

    fun sendVerifyCode(key: String, code: String) {
        CoroutineScope(Dispatchers.IO).launch {
            TODO("unimplementated!")
        }
    }
    //移除超过60秒的验证码
    fun tryCleanVerifyCode() {
        val now = System.currentTimeMillis()
        verifyMap.forEach {
            if (now - it.value.second > 60 * 1000) {
                verifyMap.remove(it.key)
            }
        }
    }
}