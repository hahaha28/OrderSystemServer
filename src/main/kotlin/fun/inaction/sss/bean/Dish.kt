package `fun`.inaction.sss.bean

import org.bson.types.ObjectId

data class Dish(
    var name:String,
    /**
     * 定价
     */
    var price:Float,
    /**
     * 分组ID
     */
    var groupID:String
) {
    /**
     * 菜品id
     */
    var _id:ObjectId? = null



    /**
     * 图片的磁盘路径
     */
    var picPath:String = "images/dish_default.png"
    /**
     * 图片的url路径
     */
    var pic: String = "assets/dish_default.png"
    /**
     * 描述
     */
    var description:String = ""

    /**
     * 材料列表
     */
    var ingredient:List<Ingredient>? = null

    /**
     * 销量
     */
    var sales:Int = 0

    /**
     * 好评率
     */
    var positiveRate:Float = 0f
}

data class Ingredient(
    /**
     * 名称
     */
    var inName:String,
    /**
     * 单位
     */
    var inUnit:String,
    /**
     * 需要多少
     */
    var inNeed:Float
)