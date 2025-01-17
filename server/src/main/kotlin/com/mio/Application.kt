package com.mio

import bean.KmmResponse
import bean.User
import com.google.gson.Gson
import com.mio.routing.userRouting
import configureDatabases
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {

    install(CORS) {
        anyHost() // 允许所有来源，生产环境中请限制为特定来源
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
    }

    configureDatabases()
    userRouting()


    routing {
        ImageRouting()



        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        // 登录
        post("/login") {
            val name = call.request.queryParameters["name"]
            val pwd = call.request.queryParameters["pwd"]
            println("name: $name, pwd: $pwd")

            val user = User(1, "mio", "123456")
            call.callback(user)

        }


    }


}


val gson by lazy { Gson() }

suspend fun ApplicationCall.callback(any: Any) {
    respondText(gson.toJson(any), ContentType.Application.Json)
}

suspend fun ApplicationCall.callSuccess(any: Any) {
    callback(KmmResponse(200, "success", any))
}

suspend fun ApplicationCall.callError(any: Any) {
    callback(KmmResponse(500, "error", any))
}