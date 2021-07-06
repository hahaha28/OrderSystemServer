package `fun`.inaction.sss.bean

import org.bson.types.ObjectId

data class Order(
    var _id:ObjectId? = null,
    var customerID:String,
    var restaurantID:String,
    var dishes:MutableList<OrderDish>,
    /**
     * 总价格
     */
    var cost:Float,
    /**
     * 取餐码
     */
    var takeFoodCode:String,
    /**
     * 订单创建时间
     */
    var createTime :Long,
    /**
     * 完成时间
     */
    var finishTime:Long = 0,
    var status:Int, // 状态，0已完成，1进行中
){
    var dishData:MutableList<OrderDishDetail>? = null
}

data class OrderDish(
    var dishID:String,
    var num:Int
)
data class OrderDishDetail(
    var dish:Dish,
    var num:Int
)

