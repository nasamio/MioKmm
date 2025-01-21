package com.mio.pages.ai

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.core.ListResponse
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.mio.pages.ai.aiState.openAI
import com.mio.pages.ai.aiState.sendMessage
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.ic_qq
import miokmm.composeapp.generated.resources.ic_user
import org.jetbrains.compose.resources.painterResource
import utils.NetHelper
import kotlin.time.Duration.Companion.seconds

@Composable
@Preview
fun aiUi() {
    val chatMessage by aiState.chatMessage.collectAsState()
    val isAiResponding by aiState.isAiResponding.collectAsState()
    val model by aiState.model.collectAsState()

    val contentBgColor by Config.contentBgColor.collectAsState()
    val state = rememberLazyListState()

    LaunchedEffect(1) {

//        val models = openAI.models()
//        println("aiUi: 共支持${models.size}个模型,分别是${models.joinToString { it.toString() }}")

        delay(1_000L)
        sendMessage("你好")
//        delay(3_000L)
//        sendMessage("100+100等于多少")
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = model.id,
                    color = Color.White,
                )
            }
        },
//        floatingActionButton = {
//            Button(onClick = {}) {
//                Text(if (isAiResponding) "ai回答中..." else "button")
//            }
//        },
        bottomBar = {
            BottomBar {
                sendMessage(it) {
                    state.scrollToItem(chatMessage.size - 1)
                }
            }
        },
        backgroundColor = contentBgColor,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(it),
            state = state,
        ) {
            items(chatMessage.size) { index ->
                val message = chatMessage[index]
                if (message.role == ChatRole.Assistant || message.role == ChatRole.User) ChatMsgItem(message)
            }
        }
    }
}

@Composable
fun ChatMsgItem(chatMessage: ChatMessage) {
    val contentTextColor by Config.contentTextColor.collectAsState()
    val aiBgColor by Config.aiBgColor.collectAsState()
    val userBgColor by Config.userBgColor.collectAsState()

    val isUser = chatMessage.role == ChatRole.User

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_qq),
            modifier = Modifier.size(32.dp)
                .clip(CircleShape)
                .alpha(if (isUser) 0f else 1f),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Column(
            modifier = Modifier.wrapContentHeight()
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isUser) "我" else "AI",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.align(if (isUser) Alignment.TopEnd else Alignment.TopStart)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
            ) {
                chatMessage.content?.let {
                    Text(
                        modifier = Modifier.background(
                            color = if (isUser) userBgColor else aiBgColor,
                            shape = RoundedCornerShape(8.dp)
                        ).padding(8.dp),
                        text = it,
                        color = contentTextColor,
                    )
                }
            }
        }

        Image(
            painter = painterResource(Res.drawable.ic_user),
            modifier = Modifier.size(32.dp)
                .clip(CircleShape)
                .alpha(if (isUser) 1f else 0f),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}

@Composable
fun BottomBar(onSend: suspend (String) -> Unit) {
    val contentBgColor by Config.contentBgColor.collectAsState()

    var inputValue by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth()
            .height(140.dp)
            .background(color = contentBgColor)
            .drawWithContent {
                drawContent()

                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 1f),
                    strokeWidth = .2f
                )
            }.padding(8.dp)
    ) {
        BasicTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
            },
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .onKeyEvent {
                    return@onKeyEvent when (it.key) {
                        Key.Enter -> {
                            // 如果不处于 回答中，才发送消息
                            scope.launch {
//                                if (!aiState.isAiResponding.value) {
                                val newContent = inputValue.removeSuffix("\n")
                                inputValue = ""
                                onSend(newContent)
//                                }

                            }
                            true
                        }

                        else -> {
                            false
                        }
                    }
                },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(50.dp)
                        .padding(8.dp),
                ) {
                    innerTextField()

                    Text(
                        text = inputValue.length.toString(),
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.align(Alignment.BottomEnd),
                    )
                }
            },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
            ),
            cursorBrush = SolidColor(Color.White.copy(alpha = 0.5f)),
        )
    }
}
