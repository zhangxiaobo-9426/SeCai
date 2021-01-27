package com.example.secai.ui.world.article

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.gallerydemo2.utils.VolleySingleton
import com.example.secai.ui.world.article.entity.Article
import com.example.secai.ui.world.article.entity.PhotoItem
import com.google.gson.Gson

enum class NetworkStatus{
    //加载中
    LOADING,
    //失败
    FAILED,
    //加载完毕
    COMPLETED,
    //初次加载数据
    INITIAL_LOADING,
    //表示一个页面加载完毕
    LOADED
}
class ArticleDataSource(private val context: Context): PageKeyedDataSource<Int, PhotoItem>() {
    var retry :(()-> Any)? =null
    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus : LiveData<NetworkStatus> = _networkStatus

    private val queryKey = arrayOf("encourage","friendship","positive","beauty","photo","confident","optimistic","straightforward","enthusiastic").random()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoItem>
    ) {
        retry = null
        _networkStatus.postValue(NetworkStatus.INITIAL_LOADING)

        val url = "https://pixabay.com/api/?key=15842702-ebfe48dd31b3ef33f8fa85566&q=${queryKey}&per_page=50&page=1"
        StringRequest(
            Request.Method.GET,
            url,
            Response.Listener {
                val dataList = Gson().fromJson(it,
                    Article::class.java).hits.toList()
                callback.onResult(dataList,null,2)
                _networkStatus.postValue(NetworkStatus.LOADED)
            },
            Response.ErrorListener {
                //37.2
                retry = { loadInitial(params,callback)}
                //33.3
                _networkStatus.postValue(NetworkStatus.FAILED)
                Log.d("hello","loadInital: $it")
            }
        ).also { VolleySingleton.getInstance(
            context
        ).requestQueue.add(it) }
    }
    //加载下一页
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        retry = null
        _networkStatus.postValue(NetworkStatus.LOADING)
        val url = "https://pixabay.com/api/?key=15842702-ebfe48dd31b3ef33f8fa85566&q=${queryKey}&per_page=50&page=${params.key}"
        StringRequest(
            Request.Method.GET,
            url,
            Response.Listener {
                val dataList = Gson().fromJson(it,
                    Article::class.java).hits.toList()
                callback.onResult(dataList,params.key+1)
                _networkStatus.postValue(NetworkStatus.LOADED)
            },
            Response.ErrorListener {
                //区分是加载新的分页。还是加载完毕
                if (it.toString() == "com.android.volley.ClientError"){
                    _networkStatus.postValue(NetworkStatus.COMPLETED)
                }else{
                    retry = {loadAfter(params,callback)}
                    _networkStatus.postValue(NetworkStatus.FAILED)
                }
                Log.d("hello","loadAfter: $it")
            }
        ).also { VolleySingleton.getInstance(
            context
        ).requestQueue.add(it) }
    }
    //加载上一页
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {

    }
}