package com.mio.pages.ai

import androidx.compose.ui.graphics.Color
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.core.FinishReason
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration.Companion.seconds

object aiState {
    val chatMessage = MutableStateFlow(listOf<ChatMessage>())
    val model = MutableStateFlow(ModelId("gpt-3.5-turbo"))
    val isAiResponding = MutableStateFlow(false) // 是否正在回答中

    val openAIConfig = OpenAIConfig(
        token = "sk-OFzMBS5uHNjQVur7D93920A4E17d419081BfA3D1F0D61fFf",
        timeout = Timeout(socket = 60.seconds),
        host = OpenAIHost("https://api.vveai.com/v1/"),
    ) {
        install(ContentNegotiation) {
            json()
        }
    }

    val openAI = OpenAI(openAIConfig)

    suspend fun sendMessage(message: String, onAddMsg: suspend () -> Unit = {}) {
        chatMessage.value += ChatMessage(ChatRole.User, message)
        onAddMsg()

        isAiResponding.value = true
        openAI.chatCompletions(
            ChatCompletionRequest(
                model = model.value,
                messages = chatMessage.value + ChatMessage(ChatRole.User, message)
            )
        ).collect {
            it.choices.forEach { choice ->
                // 更新回复状态
                if (choice.finishReason == FinishReason.Stop) {
                    isAiResponding.value = false
                    return@collect
                }

                println("sendMessage: $choice")
                if (choice.delta?.role == ChatRole.Assistant) {
                    // 添加一个新的信息
                    chatMessage.value += ChatMessage(ChatRole.Assistant, choice.delta?.content)
                } else {
                    // 更新最后一个信息的内容
                    val lastMessage = chatMessage.value.lastOrNull()
                    if (lastMessage != null && lastMessage.role == ChatRole.Assistant) {
                        chatMessage.value = chatMessage.value.dropLast(1) + ChatMessage(
                            ChatRole.Assistant,
                            lastMessage.content + choice.delta?.content
                        )
                    }
                }
                onAddMsg()
            }
        }
    }
}

object Config {
    val contentTextColor = MutableStateFlow(Color(0xffffffff))
    val userBgColor = MutableStateFlow(Color(0xff21413b))
    val aiBgColor = MutableStateFlow(Color(0xff373739))
    val contentBgColor = MutableStateFlow(Color(0xff27282a))
}

