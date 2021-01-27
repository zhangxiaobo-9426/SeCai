package com.example.gallerydemo2.utils

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
//五、数据加载，单例，辅助类，，，让全局只有一个VolleySingleton的队列
/**外部通过VolleySingleton.getInstance.requestQueue获取**/

//private  constructor(context:Context)不能生成实例
class VolleySingleton private  constructor(context:Context){
    //静态的方法
    companion object{
        private  var INSTANCE : VolleySingleton?=null
        //synchronized（）保证进程安全，防止多个进程碰撞
        fun getInstance(context:Context) =
            INSTANCE
                ?: synchronized(this){
                VolleySingleton(context)
                    .also { INSTANCE = it }
            }

    }

    //成员
    val requestQueue:RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}