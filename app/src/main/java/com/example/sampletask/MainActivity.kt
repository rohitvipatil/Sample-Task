package com.example.sampletask


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.sampletask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: VideoAdapter
    private val videos = ArrayList<Video>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videos.add(
            Video(
                "https://cdn.pixabay.com/vimeo/328940142/Buttercups%20-%2022634.mp4?width=1304&hash=2df4ff27ac821dcb2174355e8051bd782697fcb4"            )
        )

        videos.add(
            Video(
                "https://cdn.pixabay.com/vimeo/452154245/Butterfly%20-%2047876.mp4?width=640&hash=df06410fb1d2b25c9e84f092c3a2dd006cd605f9"            )
        )

        videos.add(
            Video(
                "https://cdn.pixabay.com/vimeo/230853041/Sunflower%20Field%20-%2011496.mp4?width=1280&hash=eb6dc8a0c76764fc752a26ac287b434d23783c4f"            )
        )

        adapter = VideoAdapter(this, videos, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        })

        binding.viewPager2.adapter = adapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
}