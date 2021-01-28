package com.example.secai.ui.recommend

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.secai.R
import com.example.secai.ui.recommend.entity.RecommendItme
import kotlinx.android.synthetic.main.recommend_cell.view.*
import kotlinx.android.synthetic.main.recommend_footer.view.*


class RecommendAdapter(private val recommendViewModel: RecommendViewModel) :
    PagedListAdapter<RecommendItme, RecyclerView.ViewHolder>(DIFFCALLBACK) {
    private var recommendNetworkStatus: RecommendNetworkStatus? = null
    private var hasRecommendFooter = false

    init {
        recommendViewModel.recommendretry()
    }

    private fun hidFooter() {
        if (hasRecommendFooter) {
            notifyItemRemoved(itemCount - 1)
        }
        hasRecommendFooter = false
    }

    private fun showFooter() {
        if (hasRecommendFooter) {
            notifyItemChanged(itemCount - 1)
        } else {
            hasRecommendFooter = true
            notifyItemInserted(itemCount - 1)
        }
    }

    fun updateNetworkStatus(recommendNetworkStatus: RecommendNetworkStatus?) {
        this.recommendNetworkStatus = recommendNetworkStatus
        if (recommendNetworkStatus == RecommendNetworkStatus.RecommendINITIAL_LOADING) hidFooter() else showFooter()
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasRecommendFooter) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasRecommendFooter && position == itemCount - 1) R.layout.recommend_footer else R.layout.recommend_cell
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.recommend_cell -> RecommendViewHolder.newRecommendInstance(parent).also { holder ->
                holder.itemView.setOnClickListener {
                    Bundle().apply {
                        putInt("Recommend_POSITION", holder.adapterPosition)
                        holder.itemView.findNavController()
                            .navigate(R.id.action_RecommendFragment_to_pagerRecommendFragment, this)
                    }
                }
            }
            else -> RecommendFooterViewHolder.newRecommendIstance(parent).also {
                it.itemView.setOnClickListener {
                    recommendViewModel.recommendretry()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.recommend_footer -> (holder as RecommendFooterViewHolder).bindWithRecommendNetwoekStatus(
                recommendNetworkStatus
            )
            else -> {
                val recommendItme = getItem(position) ?: return
                (holder as RecommendViewHolder).bindwithRecommendItem(recommendItme)
            }
        }
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<RecommendItme>() {
        //2.1判断两个Item是否相同
        override fun areContentsTheSame(oldItem: RecommendItme, newItem: RecommendItme): Boolean {
            return oldItem == newItem
        }

        //2.2判断两个Item内容是否相同，使用id,因为id唯一
        override fun areItemsTheSame(oldItem: RecommendItme, newItem: RecommendItme): Boolean {
            return oldItem.recommendId == newItem.recommendId
        }
    }
}

class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newRecommendInstance(parent: ViewGroup): RecommendViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.recommend_cell, parent, false)
            return RecommendViewHolder(view)
        }
    }

    fun bindwithRecommendItem(recommendItme:RecommendItme) {
        with(itemView) {
            shimmerLayoutRecommend.apply {
                setShimmerColor(0x55FFFFFF)
                setShimmerAngle(0)
                startShimmerAnimation()
            }
            textViewrecommendAuthor.text = recommendItme.recommendAuthor
            textViewrecommendTitle.text = recommendItme.recommendTitle
            textViewrecommendCreatedAt.text = recommendItme.recommendCreatedAt
            textViewrecommendDesc.text = recommendItme.recommendDesc
            textViewrecommendViews.text = recommendItme.recommendViews.toString()
            textViewrecommendLikeCounts.text = recommendItme.recommendLikeCounts.toString()

        }
        try {
            Glide.with(itemView)
                .load(recommendItme.recommendImages[0])
                .placeholder(R.drawable.ic_photo_gallery_24dp)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false.also { itemView.shimmerLayoutRecommend?.stopShimmerAnimation() }
                    }

                }).into(itemView.imageViewRecommendCell)
        }        catch (exception:Exception){
            itemView.imageViewRecommendCell.visibility = View.GONE
        }

    }
}

class RecommendFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newRecommendIstance(parent: ViewGroup): RecommendFooterViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.recommend_footer, parent, false)
            return RecommendFooterViewHolder(view)
        }
    }

    fun bindWithRecommendNetwoekStatus(recommendNetworkStatus: RecommendNetworkStatus?) {
        with(itemView) {
            when (recommendNetworkStatus) {
                RecommendNetworkStatus.RecommendFAILED -> {
                    textViewRecommendFooter.text = "点击重试"
                    progressBar2.visibility = View.GONE
                    isClickable = true
                }
                RecommendNetworkStatus.RecommendCOMPLETED -> {
                    textViewRecommendFooter.text = "加载完毕"
                    progressBar2.visibility = View.GONE
                    isClickable = false
                }
                else -> {
                    textViewRecommendFooter.text = "正在加载"
                    progressBar2.visibility = View.VISIBLE
                    isClickable = false
                }
            }
        }
    }
}