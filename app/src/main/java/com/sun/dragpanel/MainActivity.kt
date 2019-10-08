package com.sun.dragpanel

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_video_sample.*

class MainActivity : AppCompatActivity() {
    private val APPLICATION_RAW_PATH = "android.resource://com.sun.dragpanel/"
    private val VIDEO_POSTER =
        "http://wac.450f.edgecastcdn.net/80450F/screencrush.com/files/2013/11/the-amazing-spider-" + "man-2-poster-rhino.jpg"
    private val VIDEO_THUMBNAIL =
        "http://wac.450f.edgecastcdn.net/80450F/screencrush.com/files/2013/11/the-amazing-spider-" + "man-2-poster-green-goblin.jpg"
    private val VIDEO_TITLE = "The Amazing Spider-Man 2: Rise of Electro"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_sample)
        initializeVideoView()
        initializePoster()
        hookDraggableViewListener()
        iv_poster.setOnClickListener {
            draggable_view.maximize()
        }
    }

    private fun hookDraggableViewListener() {
        draggable_view.setDraggableListener(object : DraggableListener {
            override fun onMaximized() {
                startVideo()
            }

            //Empty
            override fun onMinimized() {
                //Empty
            }

            override fun onClosedToLeft() {
                pauseVideo()
            }

            override fun onClosedToRight() {
                pauseVideo()
            }
        })
    }
    private fun pauseVideo() {
        if (video_view.isPlaying) {
            video_view.pause()
        }
    }

    /**
     * Resume the VideoView content.
     */
    private fun startVideo() {
        if (!video_view.isPlaying) {
            video_view.start()
        }
    }

    private fun initializePoster() {
        val path = Uri.parse(APPLICATION_RAW_PATH + R.raw.video)
        video_view.setVideoURI(path)
        video_view.start()
    }

    private fun initializeVideoView() {
        Picasso.get()
            .load(VIDEO_POSTER)
            .placeholder(R.mipmap.ic_launcher)
            .into(iv_poster)
        Picasso.get()
            .load(VIDEO_THUMBNAIL)
            .placeholder(R.mipmap.ic_launcher)
            .into(iv_thumbnail)
    }

    override fun onBackPressed() {
        draggable_view.minimize()
    }

}
