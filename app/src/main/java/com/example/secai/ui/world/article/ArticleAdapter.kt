package com.example.secai.ui.world.article

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.secai.R
import com.example.secai.ui.world.article.entity.PhotoItem
import kotlinx.android.synthetic.main.article_cell.view.*
import kotlinx.android.synthetic.main.article_footer.view.*

class ArticleAdapter(private val galleryViewModel: ArticleViewModel) : PagedListAdapter<PhotoItem, RecyclerView.ViewHolder>(
    DIFFCALLBACK
) {


    private var networkStatus: NetworkStatus? = null
    //是否加载更多
    private var hasFooter = false
    init {
        galleryViewModel.retry()
    }

    private fun hidFooter(){
        if (hasFooter){
            notifyItemRemoved(itemCount -1 )
        }
        hasFooter = false
    }
    private fun showFooter(){
        if (hasFooter){
            notifyItemChanged(itemCount -1)
        }else{
            hasFooter =true
            notifyItemInserted(itemCount -1)
        }
    }
    fun updateNetworkStatus(networkStatus: NetworkStatus?){
        this.networkStatus = networkStatus
        if (networkStatus == NetworkStatus.INITIAL_LOADING) hidFooter() else showFooter()
    }

    //返回数据多一个（显示加载更多），
    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 1 else 0
    }
    //如果是最后一行就显示加载更多，否则加载图片
    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount -1) R.layout.article_footer else R.layout.article_cell
    }


        //自动生成主体，编写onCreateViewHolder和onBindViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType){
                R.layout.article_cell -> PhotoViewHodler.newIstance(
                    parent
                ).also { holder->
                    holder.itemView.setOnClickListener {
                        Bundle().apply {
                            putInt("PHOTO_POSITION",holder.adapterPosition)
                            holder.itemView.findNavController().navigate(R.id.action_WorldFragment_to_pagerArticleFragment,this)
                    }
                }
             }
                else -> FooterViewHodler.newIstance(
                    parent
                ).also {
                    it.itemView.setOnClickListener {
                    galleryViewModel.retry()
                    }
                }

            }

    }

//        先加载一个占位符，让它闪动起来，然后网络加载图片，加载好后闪动停止
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    when (holder.itemViewType) {
        R.layout.article_footer -> (holder as FooterViewHodler).bindWithNetwoekStatus(networkStatus)
        else -> {
            val photoItem = getItem(position) ?: return
            (holder as PhotoViewHodler).bindwithPhotoItem(photoItem)
        }
    }
}

        //编写DIFFCALLBACK比较器
    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoItem>() {
            //判断两个Item是否相同
        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
            //判断两个Item内容是否相同，使用id,因为id唯一
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }
    }
}

//编写PhotoViewHodler
class PhotoViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView){
    companion object{
        fun newIstance(parent: ViewGroup): PhotoViewHodler {
            val  view =LayoutInflater.from(parent.context).inflate(R.layout.article_cell,parent,false)
            return PhotoViewHodler(view)
        }
    }
       fun bindwithPhotoItem(photoItem: PhotoItem){
           with(itemView){
               //占位符先闪动,设置颜色,角度,开始闪动
               shimmerLayoutcell.apply {
                   setShimmerColor(0x55FFFFFF)
                   setShimmerAngle(0)
                   startShimmerAnimation()
               }

               //增加三个字段
               textViewUser.text = photoItem.photoUser
               textViewLikes.text = photoItem.photoLikes.toString()
               textViewFavorites.text = photoItem.photoFavorites.toString()

               //十九、防止瀑布布局重拍，Pixabay.kt中引入webformatHeight字段
               imageView.layoutParams.height = photoItem.photoHeight

           }

//    3.3网络加载图片,with加载的位置,load网址,listener监听器(成功或者失败,自动生成,只改return false),placeholder占位符,into加载到那个组件
           Glide.with(itemView)
               //31.4 修改 .load(getItem(position).previewURL)
               .load(photoItem.previewURL)
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
                       return false.also { itemView.shimmerLayoutcell?.stopShimmerAnimation() }
                   }

               }).placeholder(R.drawable.ic_photo_gallery_24dp).into(itemView.imageView)
       }
 }

//40.2 编写FooterViewHodler
class FooterViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView){
    companion object{
        fun newIstance(parent: ViewGroup): FooterViewHodler {
            val  view =LayoutInflater.from(parent.context).inflate(R.layout.article_footer,parent,false)
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            return FooterViewHodler(view)
        }
    }
    fun bindWithNetwoekStatus(networkStatus: NetworkStatus?){
        with(itemView){
            when(networkStatus){
                NetworkStatus.FAILED ->{
                    textViewfooter.text = "点击重试"
                    progressBar.visibility = View.GONE
                    isClickable = true
                }
                NetworkStatus.COMPLETED ->{
                    textViewfooter.text = "加载完毕"
                    progressBar.visibility = View.GONE
                    isClickable = false
                }
                else ->{
                    textViewfooter.text = "正在加载"
                    progressBar.visibility = View.VISIBLE
                    isClickable = false
                }
            }
        }
    }
}