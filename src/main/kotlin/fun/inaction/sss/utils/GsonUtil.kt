package `fun`.inaction.sss.utils

import com.google.gson.GsonBuilder
import org.bson.types.ObjectId

val objectIdGson = GsonBuilder()
    .registerTypeAdapter(ObjectId::class.java,ObjectIdSerializer())
    .registerTypeAdapter(ObjectId::class.java,ObjectIdDeserializer())
    .create()