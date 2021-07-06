package `fun`.inaction.sss.routes

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.assetsRoute(){
    routing {
        static("img"){
            files("images")
        }
    }
}