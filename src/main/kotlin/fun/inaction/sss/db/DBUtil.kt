package `fun`.inaction.sss.db

import `fun`.inaction.sss.bean.Dish
import `fun`.inaction.sss.bean.DishGroup
import `fun`.inaction.sss.bean.Order
import `fun`.inaction.sss.routes.findOrders
import `fun`.inaction.sss.utils.objectIdGson
import `fun`.inaction.sss.utils.toJson
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.and
import org.bson.Document
import org.bson.types.ObjectId
import redis.clients.jedis.Jedis
import java.text.SimpleDateFormat
import java.util.*

private val userName = "hahaha28"
private val password = "S28dkdfcdmkmfcm"
private val url = "8.129.24.81:27017"

val dbClient =MongoClients.create("mongodb://$userName:$password@$url/?authSource=admin")
val db = dbClient.getDatabase("orderSystem")

val userTable = db.getCollection("user")
val dishTable = db.getCollection("dish")
val dishGroupTable = db.getCollection("dishGroup")
val orderTable = db.getCollection("order")
val suggestionTable = db.getCollection("suggestion")



fun main(){
//    findOrders(1621478604180,1621479120717).forEach {
//        println(it)
//    }
}