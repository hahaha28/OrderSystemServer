package `fun`.inaction.sss.db


import `fun`.inaction.sss.utils.objectIdGson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mongodb.client.MongoCollection
import org.bson.Document

inline fun <reified T> MongoCollection<Document>.findAll(
    document: Document,
    convertID: Boolean = false
): List<T> {
    val result = mutableListOf<T>()

    this.find(document).forEach {
        if (convertID) {
            it.replace("_id", it.getObjectId("_id").toHexString())
        }

        val jsonStr = objectIdGson.toJson(it)
        val obj = objectIdGson.fromJson<T>(jsonStr, object : TypeToken<T>() {}.type)
        result.add(obj)
    }
    return result
}


inline fun <reified T> MongoCollection<Document>.findAll(
    key: String,
    value: Any,
    convertID: Boolean = false
): List<T> = this.findAll<T>(Document(key,value),convertID)


inline fun <reified T> MongoCollection<Document>.findAll(
    map:Map<String,Any>,
    convertID: Boolean = false
):List<T> = this.findAll(Document(map),convertID)


inline fun <reified T> MongoCollection<Document>.findOne(
    document: Document,
    convertID: Boolean = false
):T?{
    val result = this.findAll<T>(document, convertID)
    return if(result.isNotEmpty()){
        result[0]
    }else{
        null
    }
}

inline fun <reified T> MongoCollection<Document>.findOne(
    key: String,
    value: Any,
    convertID: Boolean = false
):T? = this.findOne(Document(key,value),convertID)

inline fun <reified T> MongoCollection<Document>.findOne(
    map:Map<String,Any>,
    convertID: Boolean = false
):T? = this.findOne(Document(map),convertID)

