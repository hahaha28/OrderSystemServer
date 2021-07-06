package `fun`.inaction.sss.utils

import sun.rmi.runtime.Log
import java.io.*
import kotlin.random.Random

object FileUtil {

    fun writeImageFile(inputStream:InputStream,type:String):String{
        val bis = BufferedInputStream(inputStream)
        val path = getRandomImageFileName(type)
        val file = File(path)
        file.createNewFile()
        val fos = FileOutputStream(file)
        val bos = BufferedOutputStream(fos)

        val bytes = ByteArray(1024*20)
        var len :Int
        while( (bis.read(bytes).also { len = it }) != -1){
            bos.write(bytes,0,len)
        }
        bos.close()
        return path
    }

    private fun getRandomImageFileName(type:String):String{

        var randomNum:Int
        val files = File("images").listFiles()
        println("size = ${files?.size}")
        var isBreak:Boolean
        do {
            isBreak = true
            randomNum = Random.nextInt(1,100000)
            files.forEach {
                if(it.name.equals("${randomNum}.${type}")){
                    isBreak = false
                    return@forEach
                }
            }
        }while (!isBreak)
        return "images/${randomNum}.${type}"
    }

}