package com.example.secai.ui.world.video


import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope

import com.example.secai.R
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class PlayerFragment(private val url :String) : Fragment() {
    private val mediaPlayer = MediaPlayer()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer.apply {
            setOnPreparedListener {
                player_progressBarH.max = mediaPlayer.duration
//                it.start()
                seekTo(1)
                player_progressBar.visibility = View.INVISIBLE
            }
            setDataSource(url)
            prepareAsync()
            player_progressBar.visibility = View.VISIBLE

        }
        lifecycleScope.launch {
            while (true){
                player_progressBarH?.progress = mediaPlayer.currentPosition
                delay(500)
            }
        }
        player_surfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
                mediaPlayer.setDisplay(p0)
                mediaPlayer.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(p0: SurfaceHolder?) {
                //TODO("Not yet implemented")
            }

            override fun surfaceCreated(p0: SurfaceHolder?) {
                //TODO("Not yet implemented")
            }

        })
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
        //守护进程，保证第一个视频加载
        lifecycleScope.launch {
            while (!mediaPlayer.isPlaying){
                mediaPlayer.start()
                delay(500)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

}
