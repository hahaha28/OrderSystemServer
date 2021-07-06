package `fun`.inaction.sss.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

suspend fun ApplicationCall.respond(obj: Any, code: Int, msg: String) {

    val jsonEl = objectIdGson.toJsonTree(obj)
    val jsonObj = jsonEl.asJsonObject
    jsonObj.addProperty("code", code)
    jsonObj.addProperty("msg", msg)
    val json = jsonObj.toString()
    this.respondText(json, ContentType.parse("application/json"))
}

suspend fun ApplicationCall.respond(code: Int, msg: String) {
    val jsonObj = JsonObject()
    jsonObj.addProperty("code", code)
    jsonObj.addProperty("msg", msg)
    val json = jsonObj.toString()
    this.respondText(json, ContentType.parse("application/json"))
}

suspend fun ApplicationCall.respond(map: Map<String, Any>, code: Int, msg: String) {
    val jsonObj = objectIdGson.toJsonTree(map).asJsonObject
    jsonObj.addProperty("code", code)
    jsonObj.addProperty("msg", msg)
    val json = jsonObj.toString()
    this.respondText(json, ContentType.parse("application/json"))
}

suspend fun ApplicationCall.respondSuccess(obj: Any) = respond(obj,200,"ok")
suspend fun ApplicationCall.respondSuccess() = respond(200,"ok")
suspend fun ApplicationCall.respondSuccess(map: Map<String, Any>) = respond(map,200,"ok")

fun Any.toJson():String{
    val gson = Gson()
    return gson.toJson(this)
}

inline fun <reified T> String.toObj():T{
    val gson = Gson()
    return gson.fromJson<T>(this, object : TypeToken<T>(){}.type)
}

fun Any.addField(key:String,value:Any):JsonObject{
    val gson = Gson()
    val tree = gson.toJsonTree(this)
    val jsonObj = tree.asJsonObject
    jsonObj.add(key,gson.toJsonTree(value))
    return jsonObj
}

fun JsonObject.addField(key:String,value:Any):JsonObject{
    val gson = Gson()
    this.add(key,gson.toJsonTree(value))
    return this
}

fun Any.removeField(key:String):JsonObject{
    val gson = Gson()
    val jsonObj = gson.toJsonTree(this).asJsonObject
    jsonObj.remove(key)
    return jsonObj
}

fun JsonObject.removeField(key:String):JsonObject{
    this.removeField(key)
    return this
}

fun main(){

}