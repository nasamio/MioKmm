package com.mio.pages.login

import com.mio.pages.main.MainState
import kotlinx.coroutines.flow.MutableStateFlow

object LoginState {
    fun login(name: String, pwd: String) {
        println("login: name:$name,pwd:$pwd")
        if (name == "mio" && pwd == "123456"){
            isLogin.value = true
            MainState.toast("登录成功")
        }else{
            println("login: 登录失败...")
        }
    }

    val isLogin = MutableStateFlow(false)
}