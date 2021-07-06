package `fun`.inaction.sss

import `fun`.inaction.sss.routes.*
import `fun`.inaction.sss.utils.config
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory


fun main() {
    embeddedServer(Netty, port = config.port, host = config.host) {
        install(ContentNegotiation){
            gson()
        }
        install(CallLogging)
        install(StatusPages){
            status(HttpStatusCode.Accepted){

            }
        }

        disableMongodbLog()

        routing {
            get("/"){
                call.respondText("ok")
            }
        }

        userRoute()
        dishesRoute()
        orderRoute()
        suggestionRout()
        assetsRoute()

    }.start(wait = true)
}

/**
 * 关掉mongoDB的log
 */
fun disableMongodbLog(){
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    val rootLogger: Logger = loggerContext.getLogger("org.mongodb.driver")
    rootLogger.level = Level.OFF
}
