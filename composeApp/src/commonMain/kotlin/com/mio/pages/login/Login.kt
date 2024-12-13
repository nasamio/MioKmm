package com.mio.pages.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.compose_multiplatform
import miokmm.composeapp.generated.resources.invis
import miokmm.composeapp.generated.resources.vis
import org.jetbrains.compose.resources.painterResource

/**
 *  居中显示 名称 密码输入框 + 登录按钮
 */
@Composable
fun LoginUi() {
    var username by remember { mutableStateOf(TextFieldValue("mio")) }
    var password by remember { mutableStateOf(TextFieldValue("123456")) }
    var passwordVisible by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 名称输入框，限制为英文输入
            TextField(
                value = username,
                onValueChange = {
                    // 只允许输入英文字符
                    if (it.text.all { char -> char.isLetter() }) {
                        username = it
                    }
                },
                label = { Text("用户名") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.widthIn(240.dp, 400.dp)
            )

            // 密码输入框
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    // 眼睛图标，用于切换密码可见性
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(if (passwordVisible) Res.drawable.vis else Res.drawable.invis),
                            null
                        )
                    }
                },
                modifier = Modifier.widthIn(240.dp, 400.dp)
            )

            // 登录按钮
            Button(
                onClick = { LoginState.login(username.text, password.text) },
                modifier = Modifier.widthIn(120.dp, 200.dp)
            ) {
                Text("登录")
            }
        }
    }
}
