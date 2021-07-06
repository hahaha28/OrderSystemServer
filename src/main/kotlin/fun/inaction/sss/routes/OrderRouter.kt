package `fun`.inaction.sss.routes

import `fun`.inaction.sss.bean.*
import `fun`.inaction.sss.db.*
import `fun`.inaction.sss.utils.TakeFoodCodeUtil
import `fun`.inaction.sss.utils.objectIdGson
import `fun`.inaction.sss.utils.respondSuccess
import com.mongodb.client.model.Filters.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import org.bson.BsonDocument
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import java.util.*
import kotlin.collections.HashMap

fun Application.orderRoute() {
    routing {

        // 用户提交订单
        post("/order/commit") {
            val order = call.receive<Order>()
            order.createTime = System.currentTimeMillis()
            order.takeFoodCode = TakeFoodCodeUtil.newCode(order.restaurantID)
            order.status = 1
            val id = orderTable.insert(order)!!
            order._id = id
            call.respondSuccess(order)
        }

        // 获取顾客的历史订单
        get("/order/customer/get") {
            val customerID = call.request.cookies["customerID"]!!
            val orders = orderTable.findAll<Order>("customerID", customerID)
                .sortedByDescending {
                    it.createTime
                }
            call.respondSuccess(
                mapOf(
                    "orders" to orders
                )
            )
        }

        // 获取店铺历史订单
        get("/order/restaurant/get") {
            val restaurantID = call.request.cookies["userID"]!!
            println("restaurantID=${restaurantID}")
            val orders = orderTable.findAll<Order>("restaurantID", restaurantID)
                .sortedByDescending {
                    it.createTime
                }
            orders.forEach {
                val dishData = mutableListOf<OrderDishDetail>()
                it.dishes.forEach { orderDish ->
                    val dish = dishTable.findOne<Dish>("_id", ObjectId(orderDish.dishID))
                    if (dish == null) {
                        println("null id:${orderDish.dishID}")
                    }
                    dishData.add(OrderDishDetail(dish!!, orderDish.num))
                }
                it.dishData = dishData
            }
            println("orders:${orders}")
            call.respondSuccess(
                mapOf(
                    "orders" to orders
                )
            )
        }

        // 订单完成
        post("/order/done") {
            val orderID = call.request.queryParameters["orderID"]!!
            orderTable.findOneAndReplace<Order>(orderID) {
                it.status = 0
                it.finishTime = System.currentTimeMillis()
            }
            call.respondSuccess()
        }

        // 某一天的营收统计
        post("/order/statistics/day") {
            val restaurantID = call.request.cookies["userID"]!!
            val forms = call.receiveParameters();
            val startTime = forms["startTime"]!!.toLong()
            val endTime = startTime+24*60*60*1000
            // 查询这段时间的订单
            val thisDayOrders = findOrders(restaurantID,startTime,endTime)

            var turnover:Float = 0f // 营业额
            val saleData = IntArray(48){ 0 } // 时间段销量统计
            val dishMap = mutableMapOf<String,OrderDish>() // 菜品销量统计
            thisDayOrders.forEach {
                // 营业额
                turnover += it.cost
                // 时间段销量统计
                val minutes = (it.createTime - startTime)/1000/60
                saleData[(minutes%30).toInt()]++
                // 菜品销量统计
                it.dishes.forEach { orderDish ->
                    val dishId = orderDish.dishID
                    if(!dishMap.containsKey(dishId)){
                        dishMap[dishId] = OrderDish(dishId,0)
                    }
                    dishMap[dishId]!!.num += orderDish.num
                }
            }
            // 找出菜品销量前三名
            val dishDatas = dishMap.values.sortedByDescending { it.num }

            // 计算前一天的营业额
            var lastTurnover:Float = 0f
            findOrders(restaurantID,startTime-24*60*60*1000,endTime-24*60*60*1000).forEach {
                lastTurnover += it.cost
            }
            // 汇总结果
            val result = Statistics()
            result.orderSum = thisDayOrders.size
            result.turnover = turnover
            result.lastTurnover = lastTurnover
            result.saleTimeData = saleData.toMutableList()
            for(i in 0 .. 2){
                dishDatas.getOrNull(i)?.let {
                    val dish = dishTable.findOne<Dish>("_id",ObjectId(it.dishID))!!
                    result.saleTop.add(SaleTop(
                        it.dishID,dish.pic,dish.name
                    ))
                }
            }
            call.respondSuccess(result)
        }

    }
}

fun findOrders(restaurantID:String,startTime: Long, endTime: Long): List<Order> {

    val orders = mutableListOf<Order>()
    orderTable.find(and(
        gte("createTime",startTime),
        lt("createTime",endTime),
        Document("restaurantID",restaurantID)
    )).forEach {
        val jsonStr = objectIdGson.toJson(it)
        val obj = objectIdGson.fromJson<Order>(jsonStr,Order::class.java)
        orders.add(obj)
    }
    return orders
}