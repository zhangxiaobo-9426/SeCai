package com.example.secai.ui.recommend

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.secai.ui.recommend.entity.RecommendItme

class RecommendDataSourceFactory(private val context: Context) : DataSource.Factory<Int, RecommendItme>() {
    private val _recommendDataSource = MutableLiveData<RecommendDataSource>()
    val recommendDataSource: LiveData<RecommendDataSource> = _recommendDataSource
    override fun create(): DataSource<Int, RecommendItme> {
        return RecommendDataSource(context).also { _recommendDataSource.postValue(it) }
    }

}