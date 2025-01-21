package com.mio.pages.ai

import com.aallam.openai.api.chat.*
import com.mio.pages.ai.aiState.openAI
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import openBrowser

val functionMap = mutableMapOf(
    "getAge" to ::getAge,
    "open" to ::openWebPage,
)

suspend fun functionChat(str: String): ChatMessage {
    val chatMessages = mutableListOf(chatMessage {
        role = ChatRole.User
        content = str
    })
    val request = chatCompletionRequest {
        model = aiState.model.value
        messages = chatMessages
        tools { mioFunction() }
        toolChoice = ToolChoice.Auto // or ToolChoice.function("currentWeather")
    }

    val response = openAI.chatCompletion(request)
    val message = response.choices.first().message
    chatMessages.append(message)
    for (toolCall in message.toolCalls.orEmpty()) {
        require(toolCall is ToolCall.Function) { "Tool call is not a function" }
        val functionResponse = toolCall.execute()
        chatMessages.append(toolCall, functionResponse)
    }
    val secondResponse = openAI.chatCompletion(
        request = ChatCompletionRequest(model = aiState.model.value, messages = chatMessages)
    )
    return secondResponse.choices.first().message
}

fun ToolBuilder.mioFunction() {
    function(
        name = "getAge",
        description = "Get the age from one person",
    ) {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("name") {
                put("type", "string")
                put("description", "the name of one person")
            }
        }
        putJsonArray("required") {
            add("name")
        }
    }

    function(
        name = "open",
        description = "open web page from url",
    ) {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("url") {
                put("type", "string")
                put("description", "the url of target website")
            }
        }
        putJsonArray("required") {
            add("url")
        }
    }
}

/**
 * Executes a function call and returns its result.
 */
fun ToolCall.Function.execute(): String {
    val functionToCall = functionMap[function.name] ?: error("Function ${function.name} not found")
    val functionArgs = function.argumentsAsJson()
    return functionToCall(functionArgs)
}

/**
 * Appends a chat message to a list of chat messages.
 */
fun MutableList<ChatMessage>.append(message: ChatMessage) {
    add(
        ChatMessage(
            role = message.role,
            content = message.content.orEmpty(),
            toolCalls = message.toolCalls,
            toolCallId = message.toolCallId,
        )
    )
}

/**
 * Appends a function call and response to a list of chat messages.
 */
fun MutableList<ChatMessage>.append(toolCall: ToolCall.Function, functionResponse: String) {
    val message = ChatMessage(
        role = ChatRole.Tool,
        toolCallId = toolCall.id,
        name = toolCall.function.name,
        content = functionResponse
    )
    add(message)
}

fun getAge(args: JsonObject): String {
    return when (val name = args.getValue("name").jsonPrimitive.content) {
        "Alice" -> """{"name": "Alice", "age": "30"}"""
        "Bob" -> """{"name": "Bob", "age": "35"}"""
        "Charlie" -> """{"name": "Charlie", "age": "28"}"""
        else -> """{"name": "$name", "age": "unknown"}"""
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun openWebPage(args: JsonObject): String {
    val url = args.getValue("url").jsonPrimitive.content
    GlobalScope.launch {
        openBrowser(url)
    }
    return "网址[$url]已打开。"
}