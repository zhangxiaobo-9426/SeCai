package com.example.secai.ui.world.article

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.secai.ui.world.article.entity.PhotoItem

class ArticleDataSourceFactory(private val context: Context):DataSource.Factory<Int, PhotoItem>(){
    private val _articleDataSource = MutableLiveData<ArticleDataSource>()
    val articleDataSource:LiveData<ArticleDataSource> = _articleDataSource

    override fun create(): DataSource<Int, PhotoItem> {
        // 修改return articleDataSource(context)
        return ArticleDataSource(context).also { _articleDataSource.postValue(it) }
    }
}