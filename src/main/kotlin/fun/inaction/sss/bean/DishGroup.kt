package `fun`.inaction.sss.bean

import org.bson.types.ObjectId

data class DishGroup(
    var name:String,
    var userID: String,
    var dishIDs:MutableList<String> = mutableListOf(),

) {
    var _id:ObjectId? = null
    var dishList:MutableList<Dish> = mutableListOf()

}