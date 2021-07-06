package `fun`.inaction.sss.routes

import `fun`.inaction.sss.bean.Suggestion
import `fun`.inaction.sss.db.findAll
import `fun`.inaction.sss.db.findOne
import `fun`.inaction.sss.db.insert
import `fun`.inaction.sss.db.suggestionTable
import `fun`.inaction.sss.utils.respondSuccess
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.suggestionRout(){
    routing {

        post("/suggestion/commit"){
            val formData = call.receiveParameters()
            val customID = call.request.cookies["customerID"]!!
            val restaurantID = call.request.cookies["userID"]!!

            val text = formData["suggestion"]!!
            val suggestion = Suggestion(
                customerID = customID,
                restaurantID = restaurantID,
                suggestion = text,
                timestamp = System.currentTimeMillis()
            )
            val id = suggestionTable.insert(suggestion)!!
            val result = suggestionTable.findOne<Suggestion>("_id",id)!!
            call.respondSuccess(result)
        }

        get("suggestion/get"){
            val restaurantID = call.request.cookies["userID"]!!
            val data = suggestionTable.findAll<Suggestion>("restaurantID",restaurantID)
                .sortedByDescending {
                    it.timestamp
                }
            call.respondSuccess(
                mapOf(
                    "data" to data
                )
            )
        }

    }
}