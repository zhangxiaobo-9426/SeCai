package com.example.secai.ui.world.article


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.secai.R
import kotlinx.android.synthetic.main.fragment_article.*

/**
 * A simple [Fragment] subclass.
 */
class ArticleFragment : Fragment() {
    private val articleViewModel by activityViewModels<ArticleViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articleAdapter =
            ArticleAdapter(articleViewModel)
        // 设置适配器
        recyclerView.apply {
            adapter =articleAdapter
            //两行网格布局
//            layoutManager = GridLayoutManager(requireContext(),2)
            //瀑布布局
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        //30.1
        articleViewModel.pagedListLiveData.observe(viewLifecycleOwner, Observer {
            articleAdapter.submitList(it)
        })

        swipeRefreshLayoutArticle.setOnRefreshListener {
            articleViewModel.resetQuery()
        }
        //第三十六步
        articleViewModel.networkStatus.observe(viewLifecycleOwner, Observer {
            Log.d("hello","onActivityCreated：$it")
            articleAdapter.updateNetworkStatus(it)
            swipeRefreshLayoutArticle.isRefreshing =it == NetworkStatus.INITIAL_LOADING
        })

    }
}
