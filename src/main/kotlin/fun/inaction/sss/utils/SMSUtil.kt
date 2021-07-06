package `fun`.inaction.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tencentcloudapi.common.Credential
import com.tencentcloudapi.common.profile.ClientProfile
import com.tencentcloudapi.common.profile.HttpProfile

import com.tencentcloudapi.sms.v20190711.SmsClient
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse
import kotlin.random.Random


object SMSUtil {

    private const val secretId = "AKID2Rdp7rC8dstjIctU1Lyg8KE2zjCOj9h3"
    private const val secretKey = "7cvnAxOZT7bokOo1yegFXYIm0yfvuQhh"
    private const val appID = "1400505497"

    private val record = mutableMapOf<String,Pair<String,Long>>()

    /**
     * 发送验证码
     */
    fun sendSMS(tel:String):Pair<Boolean,String>{


        val cred = Credential(secretId, secretKey)

        val client = SmsClient(cred, "")

        val req = SendSmsRequest()

        req.setSmsSdkAppid(appID)
        req.setSign("等待云间月")


        req.templateID = "915265"

        val phoneNumbers = arrayOf("+86$tel")
        req.setPhoneNumberSet(phoneNumbers)

        val templateParams = arrayOf(getRandomCode(tel))
        req.setTemplateParamSet(templateParams)

        val res: SendSmsResponse = client.SendSms(req)


        if(res.sendStatusSet[0].code == "Ok"){
            return true to res.sendStatusSet[0].message
        }else{
            return false to res.sendStatusSet[0].message
        }
    }

    private fun getRandomCode(tel: String):String{
        val code = Random.nextInt(1000,9999).toString()
        record[tel] = Pair(code,System.currentTimeMillis())
        return code
    }

    fun checkCode(tel:String,code:String):Boolean{
        val deleteKeys = mutableListOf<String>()
        for((key,value) in record){
            if(value.second > System.currentTimeMillis()+1*60*1000){
                deleteKeys.add(key)
            }
        }
        deleteKeys.forEach { record.remove(it) }

        return record.containsKey(tel) && record[tel]?.first.equals(code)

    }

}

fun main(){
    SMSUtil.sendSMS("18742023625")
}