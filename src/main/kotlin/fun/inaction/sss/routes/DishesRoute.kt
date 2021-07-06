package `fun`.inaction.sss.routes

import `fun`.inaction.sss.bean.Dish
import `fun`.inaction.sss.bean.DishGroup
import `fun`.inaction.sss.db.*
import `fun`.inaction.sss.utils.FileUtil
import `fun`.inaction.sss.utils.respond
import `fun`.inaction.sss.utils.respondSuccess
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import org.bson.Document
import org.bson.types.ObjectId

fun Application.dishesRoute(){
    routing {

        // 添加分组
        post("/dish/group/add"){
            // 获取用户id
            val userID = call.request.cookies["userID"]!!
            // 获取分组名
            val formData = call.receiveParameters()
            val groupName = formData["name"]!!
            // 向数据库中添加
            val objectId = dishGroupTable.insert(DishGroup(groupName,userID))!!
            call.respondSuccess(mapOf("groupID" to objectId.toHexString()))
        }

        // 获取所有分组
        get("/dish/group/get"){
            // 获取用户id
            val userID = call.request.cookies["userID"]!!
            val dishGroups = dishGroupTable.findAll<DishGroup>("userID",userID)
            call.respondSuccess(mapOf("dishGroups" to dishGroups))
        }

        // 删除分组
        delete("/dish/group/delete"){
            val queryParams = call.request.queryParameters
            val groupID = queryParams["groupID"]!!
            val dishGroup = dishGroupTable.findOne<DishGroup>("_id",ObjectId(groupID))!!
            // 删除分组下所有菜品
            dishGroup.dishIDs.forEach {
                dishTable.deleteOne("_id",ObjectId(it))
            }
            // 删除分组
            dishGroupTable.deleteOne("_id",ObjectId(groupID))

            call.respondSuccess()
        }

        // 获取某个分组下的所有菜品
        get("/dish/get"){
            val queryParams = call.request.queryParameters
            val dishGroup = dishGroupTable.findOne<DishGroup>("_id",ObjectId(queryParams["groupID"]))
            if(dishGroup == null){
                call.respond(-1,"groupID不存在")
            }else{
                val dishList = mutableListOf<Dish>()
                dishGroup.dishIDs.forEach {
                    val dish = dishTable.findOne<Dish>("_id",ObjectId(it))!!
                    dishList.add(dish)
                }
                call.respondSuccess(mapOf("dishList" to dishList))
            }
        }

        // 获取所有菜品
        get("/dish/get/all"){
            val userID = call.request.cookies["userID"]!!
            val dishGroups = dishGroupTable.findAll<DishGroup>("userID",userID)!!
            dishGroups.forEach {
                val dishList = mutableListOf<Dish>()
                it.dishIDs.forEach {dishID->
                    val dish = dishTable.findOne<Dish>("_id",ObjectId(dishID))
                    if(dish == null){
                        println("null dish id:${dishID}")
                    }else{
                        dishList.add(dish)
                    }
                }

                it.dishList = dishList
            }
            call.respondSuccess(mapOf(
                "dishData" to dishGroups
            ))
        }

        // 添加菜品
        post("/dish/add"){
            val dish = Dish("",1f,"")
            var imagePath:String = "/images/dish_default.png"
            var jsonStr:String? = null
            call.receiveMultipart().forEachPart { part->
                when(part){
                    is PartData.FormItem -> {
                        jsonStr = part.value
                    }
                    is PartData.FileItem -> {
                        val type = part.contentType?.contentSubtype?:"null"
                        val path = FileUtil.writeImageFile(part.streamProvider(),type)
                        imagePath = path
                    }
                }
            }
            val dishDoc = Document.parse(jsonStr!!)
                .append("picPath",imagePath)
                .append("pic","/img/${imagePath.substringAfter("images/")}")
            val dishGroupID = dishDoc.getString("groupID")
            val dishId = dishTable.insert(dishDoc)!!.toHexString()
//            val dishGroup = dishGroupTable.findOne<DishGroup>("_id",ObjectId(dishGroupID))!!
//            println(dishGroup)
//            dishGroup.dishIDs.add(dishId)
//            dishGroupTable.replaceOne(mapOf("_id" to ObjectId(dishGroupID)),dishGroup)

            dishGroupTable.findOneAndReplace<DishGroup>(dishGroupID){
                it.dishIDs.add(dishId)
            }

            val dishResult = dishTable.findOne<Dish>("_id",ObjectId(dishId))
            call.respondSuccess(dishResult!!)
        }

        // 修改菜品
        post("/dish/modify"){
            var imagePath:String? = null
            var jsonStr:String? = null
            call.receiveMultipart().forEachPart { part->
                when(part){
                    is PartData.FormItem -> {
                        jsonStr = part.value
                    }
                    is PartData.FileItem -> {
                        val type = part.contentType?.contentSubtype?:"null"
                        val path = FileUtil.writeImageFile(part.streamProvider(),type)
                        imagePath = path
                    }
                }
            }
            val dishDoc = Document.parse(jsonStr!!)
            imagePath?.let {
                dishDoc.append("picPath",it)
                    .append("pic","/img/${it.substringAfter("images/")}")
            }
            val dishID = ObjectId(dishDoc.getString("_id"))
            dishDoc.remove("_id")
            println("id=${dishID}")
            println("dishDoc=${dishDoc.toJson()}")
            dishTable.findOneAndReplace(Document("_id",dishID),dishDoc)
            call.respondSuccess()
        }

        // 删除菜品
        delete("/dish/delete"){
            val dishID = call.request.queryParameters["dishID"]!!
            val dish = dishTable.findOne<Dish>("_id",ObjectId(dishID))!!
            dishTable.deleteOne("_id",ObjectId(dishID))
            dishGroupTable.findOneAndReplace<DishGroup>(dish.groupID){
                it.dishIDs.removeIf { id ->
                     id == dishID
                }
            }
            call.respondSuccess()
        }

    }
}