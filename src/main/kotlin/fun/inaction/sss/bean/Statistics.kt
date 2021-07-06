package `fun`.inaction.sss.bean

class Statistics {
    /**
     * 营业额
     */
    var turnover:Float = 0f

    /**
     * 总订单数
     */
    var orderSum:Int = 0

    /**
     * 昨天营业额
     */
    var lastTurnover:Float = 0f

    var saleTop: MutableList<SaleTop> = mutableListOf()

    var saleTimeData: List<Int>? = null

}

data class SaleTop(
    val id:String,
    val pic:String,
    val name:String
)