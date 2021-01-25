package com.example.secai.ui.world


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.secai.R
import com.example.secai.ui.world.article.ArticleFragment
import com.example.secai.ui.world.video.VideoFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_world.*

/**
 * A simple [Fragment] subclass.
 */
class WorldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_world, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        world_viewPager2.apply {
            adapter = object :FragmentStateAdapter(this@WorldFragment){
            override fun getItemCount() =2

            override fun createFragment(position: Int) =
                when(position){
                    0 ->ArticleFragment()
                    else -> VideoFragment()
                    }
                }
            setCurrentItem(0,false)
            }
        TabLayoutMediator(world_tabLayout,world_viewPager2){ tab: TabLayout.Tab, i: Int ->
            tab.text = when(i){
                0 -> "文章"
                else ->"视频"
            }
        }.attach()
        }
}
