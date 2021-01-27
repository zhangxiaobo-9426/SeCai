package com.example.secai.ui.world.article.pagerarticle


import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

import com.example.secai.R
import com.example.secai.ui.world.article.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_pager_article.*
import kotlinx.android.synthetic.main.pager_article_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
const val REQUEST_WRITE_EXTERNAL_STORAGE =1
class PagerArticleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val articleViewModel by activityViewModels<ArticleViewModel>()
        super.onViewCreated(view, savedInstanceState)
        val adapter =
            PagerArticleListAdapter()
        viewPager2.adapter =adapter
        articleViewModel.pagedListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            viewPager2.setCurrentItem(arguments?.getInt("PHOTO_POSITION") ?: 0, false)
        })

        //3.设置下面的图片编号
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                photoTag.text = getString(R.string.photo_tag, position + 1, photoList?.size)
                photoTag.text = getString(R.string.photo_tag, position + 1, articleViewModel.pagedListLiveData.value?.size)
                // 创建String资源 photoTag.text ="${position + 1}/${photoList?.size}"
            }
        })
        //4.得到传递过来的图片序号
//        viewPager2.setCurrentItem(arguments?.getInt("PHOTO_POSITION") ?: 0, false)
        //使滑动变成垂直滑动
        //viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL

        //二十四、1.为可以保存图片
        saveButton.setOnClickListener {
            //是否可以写入数据
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE
                )
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    savePhoto()
                }
            }
        }
    }

    //二十四、2.为可以保存图片
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //可挂起
                    viewLifecycleOwner.lifecycleScope.launch {
                        savePhoto()
                    }
                }else {
                    Toast.makeText(requireContext(), getString(R.string.StorageFailure), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //二十四、3.为可以保存图片，，从holder中找到图片，保存   suspend允许挂起
    private suspend fun savePhoto(){
        withContext(Dispatchers.IO){
            val holder=(viewPager2.get(0) as RecyclerView).findViewHolderForAdapterPosition(viewPager2.currentItem)
                    as PagerArticleViewHolder
            val bitmap=holder.itemView.pagerpPhoto.drawable.toBitmap()

            val saveUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )?:kotlin.run {
                MainScope().launch { Toast.makeText(requireContext(), getString(R.string.StorageFailure), Toast.LENGTH_SHORT).show() }
                return@withContext
            }
            //打开输出流
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if(bitmap.compress(Bitmap.CompressFormat.JPEG,90,it)){
                    MainScope().launch { Toast.makeText(requireContext(), getString(R.string.StorageSuccessful), Toast.LENGTH_SHORT).show() }
                }else{
                    MainScope().launch { Toast.makeText(requireContext(), getString(R.string.StorageFailure), Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }
}
