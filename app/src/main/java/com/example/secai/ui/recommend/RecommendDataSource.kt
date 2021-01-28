package com.example.secai.ui.recommend

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.gallerydemo2.utils.VolleySingleton
import com.example.secai.ui.recommend.entity.Recommend
import com.example.secai.ui.recommend.entity.RecommendItme
import com.google.gson.Gson

enum class RecommendNetworkStatus {
    //加载中
    RecommendLOADING,

    //失败
    RecommendFAILED,

    //加载完毕
    RecommendCOMPLETED,

    //初次加载数据
    RecommendINITIAL_LOADING,

    //表示一个页面加载完毕
    RecommendLOADED
}

class RecommendDataSource(private val context: Context) : PageKeyedDataSource<Int, RecommendItme>() {
    var recommendretry: (() -> Any)? = null
    private val _recommendetworkStatus = MutableLiveData<RecommendNetworkStatus>()
    val recommendnetworkStatus: LiveData<RecommendNetworkStatus> = _recommendetworkStatus

    //    private val qkey = arrayOf("Recommend").random()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, RecommendItme>
    ) {
        recommendretry = null
        _recommendetworkStatus.postValue(RecommendNetworkStatus.RecommendINITIAL_LOADING)
        val recommendUrl = "https://gank.io/api/v2/data/category/GanHuo/type/app/page/1/count/10"
        StringRequest(
            Request.Method.GET,
            recommendUrl,
            Response.Listener {
                val recommenddataList: List<RecommendItme> =
                    Gson().fromJson(it, Recommend::class.java).data.toList()
                callback.onResult(recommenddataList, null, 2)
                _recommendetworkStatus.postValue(RecommendNetworkStatus.RecommendLOADED)
            },
            Response.ErrorListener {
                recommendretry = { loadInitial(params, callback) }
                _recommendetworkStatus.postValue(RecommendNetworkStatus.RecommendFAILED)
                Log.d("recommend", "loadInitial: $it")
            }

        ).also { VolleySingleton.getInstance(context).requestQueue.add(it) }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RecommendItme>) {
        recommendretry = null
        _recommendetworkStatus.postValue(RecommendNetworkStatus.RecommendLOADING)
        val recommendUrl =
            "https://gank.io/api/v2/data/category/GanHuo/type/app/page/${params.key}/count/10"
        StringRequest(
            Request.Method.GET,
            recommendUrl,
            Response.Listener {
                val recommenddataList: List<RecommendItme> =
                    Gson().fromJson(it, Recommend::class.java).data.toList()
                callback.onResult(recommenddataList, params.key + 1)
                _recommendetworkStatus.postValue(RecommendNetworkStatus.RecommendLOADED)
            },
            Response.ErrorListener {
                if (it.toString() == "com.android.volley.ClientError") {
                    _recommendetworkStatus.postValue(RecommendNetworkStatus.RecommendCOMPLETED)
                } else {
                    recommendretry = { loadAfter(params, callback) }
                    _recommendetworkStatus.postValue(RecommendNetworkStatus.RecommendFAILED)
                }
                Log.d("recommend", "loadAfter: $it")
            }
        ).also { VolleySingleton.getInstance(context).requestQueue.add(it) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RecommendItme>) {
        TODO("Not yet implemented")
    }

}