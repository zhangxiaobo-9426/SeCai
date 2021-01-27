package com.example.secai.ui.world.article.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Article(
    val  totalHits:Int,
    val hits:Array<PhotoItem>,
    val total:Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Article

        if (totalHits != other.totalHits) return false
        if (!hits.contentEquals(other.hits)) return false
        if (total != other.total) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalHits
        result = 31 * result + hits.contentHashCode()
        result = 31 * result + total
        return result
    }
}

/**
 * 在build.gradle添加 androidExtensions{experimental = true}
 * **/
@Parcelize
data class PhotoItem(
    /**序列化**/
    //小图
    @SerializedName("webformatURL")
    val previewURL:String,
    //id
    @SerializedName("id")
    val photoId:Int,
    //大图
    @SerializedName("largeImageURL")
    val fullURL:String,

    //十九、防止瀑布布局重排，引入webformatHeight字段
    //小图的高度
    @SerializedName("webformatHeight")
    val photoHeight:Int,

    //上传的用户名
    @SerializedName("user")
    val photoUser:String,
    //喜欢人数
    @SerializedName("likes")
    val photoLikes:Int,
    //收藏人数
    @SerializedName("favorites")
    val photoFavorites:Int

): Parcelable