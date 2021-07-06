package `fun`.inaction.sss

import `fun`.inaction.sss.bean.Dish
import `fun`.inaction.sss.bean.Ingredient
import `fun`.inaction.sss.db.dishTable
import `fun`.inaction.sss.db.insert
import `fun`.inaction.sss.utils.toJson
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*

fun main2(){
    val dish = Dish(
        "水饺",
        14f,
        "60ab6aadf2337974c8cf0ca8"
    )
    dish.description = "舌尖上的美味"

    val i1 = Ingredient("水饺","把",1f)
    val i2 = Ingredient("酸菜","勺",2f)
    val i3 = Ingredient(
        "肉丝",
        "勺",
        1f
    )

    dish.ingredient = listOf(i1)

    println(dish.toJson())

}

fun main(){
    val date = Calendar.getInstance()
    date.timeInMillis = 1622379493176
    date.set(Calendar.HOUR_OF_DAY,0)
    date.set(Calendar.MINUTE,0)
    date.set(Calendar.SECOND,0)
    date.set(Calendar.MILLISECOND,0)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm s,S")
    println(sdf.format(date.time))
    println(date.timeInMillis)

}

fun after(days:Long):String{
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val startDate = sdf.parse("2020-12-09")
    val date = Date(startDate.time+days*24*60*60*1000)
    val result = sdf.format(date)
    return result
}

fun today():Long{
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val startDate = sdf.parse("2020-12-09")
//    val nowDate = sdf.parse("2020-12-11")
    var day = System.currentTimeMillis()-startDate.time
    day /= (1000 * 60 * 60 * 24)
    return day
}

data class T(val name:String,val id:String)