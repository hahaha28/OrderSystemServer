package `fun`.inaction.sss.routes

import `fun`.inaction.sss.bean.User
import `fun`.inaction.sss.db.findOne
import `fun`.inaction.sss.db.insert
import `fun`.inaction.sss.db.userTable
import `fun`.inaction.utils.SMSUtil
import `fun`.inaction.sss.utils.respond
import `fun`.inaction.sss.utils.respondSuccess
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

/**
 * 用户相关的路由
 */
fun Application.userRoute(){
    routing {

        /**
         * 登录/注册
         */
        post("/user/login"){
            val formData = call.receiveParameters()
            val tel = formData["tel"]!!
            // 首先检查验证码
            if(SMSUtil.checkCode(tel,formData["verifyCode"]!!)){
               // 验证码正确，查找数据库
                val user = userTable.findOne<User>("tel",tel)
                if(user == null){
                    // 如果用户不存在，注册
                    val userID = userTable.insert(User(tel))!!.toHexString()
                    call.respondSuccess(mapOf("userID" to userID))
                }else{
                    // 用户存在
                    call.respondSuccess(mapOf("userID" to user._id!!))
                }
            }else{
                // 验证码错误
                call.respond(-1,"验证码错误")
            }

        }

        /**
         * 发送验证码
         */
        get("/captcha/send"){
            val tel = call.request.queryParameters["tel"]!!
            val (success,msg) = SMSUtil.sendSMS(tel)
            if(success){
                call.respondSuccess()
            }else{
                call.respond(-1,msg)
            }
        }



    }
}