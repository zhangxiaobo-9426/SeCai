package com.example.secai.ui.world.article.pagerarticle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secai.R
import com.example.secai.ui.world.article.entity.PhotoItem
import kotlinx.android.synthetic.main.pager_article_view.view.*

//十七、PagerView的适配器
//1.继承ListAdapter
class PagerArticleListAdapter:ListAdapter<PhotoItem, PagerArticleViewHolder>(
    DiffCallback
){
   //3.DiffCallback
    object DiffCallback:DiffUtil.ItemCallback<PhotoItem>(){
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            //=== 判断是不是同一个对象
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
          return oldItem.photoId == newItem.photoId
        }

    }


    //4.onCreateViewHolder、onBindViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerArticleViewHolder {
         LayoutInflater.from(parent.context).inflate(R.layout.pager_article_view,parent,false).apply {
             return PagerArticleViewHolder(
                 this
             )
         }
    }

    override fun onBindViewHolder(holder: PagerArticleViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position).previewURL)
            .placeholder(R.drawable.ic_photo_gallery_24dp)
            .into(holder.itemView.pagerpPhoto)
    }
}
//2.PagerPhotoViewHolder
class PagerArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)