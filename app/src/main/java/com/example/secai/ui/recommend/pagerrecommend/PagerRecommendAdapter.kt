package com.example.secai.ui.recommend.pagerrecommend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secai.R
import com.example.secai.ui.recommend.entity.RecommendItme
import kotlinx.android.synthetic.main.pager_article_view.view.*
import kotlinx.android.synthetic.main.pager_recommend_view.view.*


class PagerRecommendAdapter : ListAdapter<RecommendItme, PagerRecommendViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<RecommendItme>() {
        override fun areItemsTheSame(oldItem: RecommendItme, newItem: RecommendItme): Boolean {
            //=== 判断是不是同一个对象
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RecommendItme, newItem: RecommendItme): Boolean {
            return oldItem.recommendId == newItem.recommendId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerRecommendViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.pager_recommend_view, parent, false).apply {
            return PagerRecommendViewHolder(
                this
            )
        }
    }

    override fun onBindViewHolder(holder: PagerRecommendViewHolder, position: Int) {
        holder.itemView.webViewPagerRecommend.getSettings().setJavaScriptEnabled(true)
        holder.itemView.webViewPagerRecommend.setWebViewClient(WebViewClient())
        //确保跳转到另一个网页时仍然在当前WebView显示
        //确保跳转到另一个网页时仍然在当前WebView显示

        holder.itemView.webViewPagerRecommend.loadUrl(getItem(position).recommendUrl)
    }
}

class PagerRecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)