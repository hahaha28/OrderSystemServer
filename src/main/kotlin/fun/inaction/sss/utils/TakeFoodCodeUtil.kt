package `fun`.inaction.sss.utils

import redis.clients.jedis.Jedis

object TakeFoodCodeUtil {

    private val redis = Jedis("8.129.24.81", 6379)

    /**
     * 获取新的取餐码
     */
    fun newCode(restaurantID: String): String {
        val oldCode = redis.get(restaurantID)
        return if (oldCode == null) {
            redis[restaurantID] = "1"
            "1"
        }else{
            redis[restaurantID] = "${oldCode.toInt()+1}"
            "${oldCode.toInt()+1}"
        }
    }

}