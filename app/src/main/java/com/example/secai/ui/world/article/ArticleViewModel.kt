package com.example.secai.ui.world.article

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData

//继承AndroidViewModel
class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val factory =
        ArticleDataSourceFactory(application)

    val pagedListLiveData = factory.toLiveData(1)
    //增加networkStatus
    val networkStatus = Transformations.switchMap(factory.articleDataSource) {it.networkStatus}
    //下拉刷新
    fun resetQuery(){
        pagedListLiveData.value?.dataSource?.invalidate()
    }
    fun retry(){
        factory.articleDataSource.value?.retry?.invoke()
    }
}