package `fun`.inaction.sss.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.bson.types.ObjectId
import java.lang.reflect.Type

class ObjectIdDeserializer:JsonDeserializer<ObjectId> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ObjectId {
        return ObjectId(json?.asJsonPrimitive?.asString)
    }
}