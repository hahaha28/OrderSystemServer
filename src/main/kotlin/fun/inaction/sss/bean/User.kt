package `fun`.inaction.sss.bean

import org.bson.types.ObjectId

data class User(
    var tel:String
){
    var _id:ObjectId? = null
}
