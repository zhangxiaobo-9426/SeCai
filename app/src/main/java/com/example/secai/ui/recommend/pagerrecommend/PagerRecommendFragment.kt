package com.example.secai.ui.recommend.pagerrecommend


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2

import com.example.secai.R
import com.example.secai.ui.recommend.RecommendViewModel
import kotlinx.android.synthetic.main.fragment_pager_recommend.*

/**
 * A simple [Fragment] subclass.
 */
class PagerRecommendFragment : Fragment() {
    val recommendViewModel by activityViewModels<RecommendViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_recommend, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PagerRecommendAdapter()
        viewPager2_PagerRecommend.adapter = adapter
        recommendViewModel.pagedRecommendListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            viewPager2_PagerRecommend.setCurrentItem(arguments?.getInt("Recommend_POSITION") ?: 0, false)

        })
        viewPager2_PagerRecommend.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                textViewPagerRecommnedTag.text = getString(
                    R.string.photo_tag,
                    position + 1,
                    recommendViewModel.pagedRecommendListLiveData.value?.size
                )
            }
        })
    }

}
