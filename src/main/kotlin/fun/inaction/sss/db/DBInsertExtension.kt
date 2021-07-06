package `fun`.inaction.sss.db


import com.google.gson.Gson
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.bson.types.ObjectId

inline fun <reified T> MongoCollection<Document>.insert(obj:T):ObjectId?{
    val gson = Gson()
    val doc = Document.parse(gson.toJson(obj))
    val result = this.insertOne(doc)
    return result.insertedId?.asObjectId()?.value
}

inline fun <reified T> MongoCollection<Document>.insertMany(list:List<T>):List<ObjectId?>{
    val gson = Gson()
    val docs = mutableListOf<Document>()
    list.forEach {
        docs.add(Document.parse(gson.toJson(it)))
    }
    val insertManyResult = this.insertMany(docs)
    val result = mutableListOf<ObjectId?>()
    insertManyResult.insertedIds.forEach { (_, id) ->
        result.add(id.asObjectId()?.value)
    }
    return result
}