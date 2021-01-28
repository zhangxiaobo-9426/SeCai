package com.example.secai.ui.recommend

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData

class RecommendViewModel(application: Application) : AndroidViewModel(application) {
    private val recommendfactory = RecommendDataSourceFactory(application)
    val pagedRecommendListLiveData = recommendfactory.toLiveData(1)
    val recommendNetworkStatus =
        Transformations.switchMap(recommendfactory.recommendDataSource) { it.recommendnetworkStatus }

    fun resetQuery() {
        pagedRecommendListLiveData.value?.dataSource?.invalidate()
    }

    fun recommendretry() {
        recommendfactory.recommendDataSource.value?.recommendretry?.invoke()
    }

}
