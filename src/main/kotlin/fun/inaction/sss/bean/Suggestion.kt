package `fun`.inaction.sss.bean

data class Suggestion(
    var _id:String? =null,
    var customerID:String,
    var restaurantID:String,
    var suggestion:String,
    var timestamp:Long
)
