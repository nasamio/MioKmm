package com.mio.routing

import com.mio.callSuccess
import io.ktor.server.application.*
import io.ktor.server.routing.*
import userService

fun Application.userRouting() {
    routing {
        get("/getAllUser") {
            userService.queryAll().let {
                call.callSuccess(it)
            }
        }
    }
}