package com.example.secai.ui.recommend.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Recommend(
    val page:Int,
    val page_count:Int,
    val status:Int,
    val total_counts:Int,
    val data:Array<RecommendItme>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recommend

        if (page != other.page) return false
        if (page_count != other.page_count) return false
        if (status != other.status) return false
        if (total_counts != other.total_counts) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = page
        result = 31 * result + page_count
        result = 31 * result + status
        result = 31 * result + total_counts
        result = 31 * result + data.contentHashCode()
        return result
    }
}

@Parcelize
data class RecommendItme(
    @SerializedName("_id")
    val recommendId:String,
    //作者
    @SerializedName("author")
    val recommendAuthor:String,
    //正文
    @SerializedName("desc")
    val recommendDesc:String,
    //喜欢人数
    @SerializedName("likeCounts")
    val recommendLikeCounts:Int,
    //创建时间
    @SerializedName("createdAt")
    val recommendCreatedAt:String,
    //标题
    @SerializedName("title")
    val recommendTitle:String,
    @SerializedName("url")
    val recommendUrl:String,
    //查看人数
    @SerializedName("views")
    val recommendViews:Int,
    @SerializedName("stars")
    val recommendStars:Int,
    @SerializedName("images")
    val recommendImages:List<String>

): Parcelable
