package `fun`.inaction.sss.utils

import com.google.gson.Gson
import java.io.File


val config: Config by lazy {
    val configJson = File("config.json").readText()
    val gson = Gson()
    gson.fromJson(configJson, Config::class.java)
}