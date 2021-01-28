package com.example.secai.ui.recommend


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
import kotlinx.android.synthetic.main.fragment_recommend.*

/**
 * A simple [Fragment] subclass.
 */
class RecommendFragment : Fragment() {
    private val recommendViewModel by activityViewModels<RecommendViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recommendAdapter = RecommendAdapter(recommendViewModel)
        recyclerViewRecommend.apply {
            adapter = recommendAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        recommendViewModel.pagedRecommendListLiveData.observe(viewLifecycleOwner, Observer {
            recommendAdapter.submitList(it)

        })
        swipeRefreshLayoutRecommend.setOnRefreshListener {
            recommendViewModel.resetQuery()
        }
        recommendViewModel.recommendNetworkStatus.observe(viewLifecycleOwner, Observer {
            Log.d("recommend", "onViewCreated:$it")
            recommendAdapter.updateNetworkStatus(it)
            swipeRefreshLayoutRecommend.isRefreshing = it == RecommendNetworkStatus.RecommendINITIAL_LOADING
        })
    }
}

