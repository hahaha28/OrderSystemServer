package `fun`.inaction.sss.db

import com.mongodb.client.MongoCollection
import com.mongodb.client.result.DeleteResult
import org.bson.Document

fun MongoCollection<Document>.deleteOne(key:String,value:Any):DeleteResult{
    return this.deleteOne(Document(key,value))
}