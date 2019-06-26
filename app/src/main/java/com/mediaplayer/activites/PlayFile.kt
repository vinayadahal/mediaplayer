package com.mediaplayer.activites

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageButton
import android.widget.TextView

import com.mediaplayer.R
import com.mediaplayer.components.PlayBackResume
import com.mediaplayer.components.PlayFileComponentInit
import com.mediaplayer.listeners.PlayFileTouchListener
import com.mediaplayer.listeners.VideoOnCompletionListener
import com.mediaplayer.listeners.VideoOnPreparedListener
import com.mediaplayer.variables.CommonArgs

import java.io.File

class PlayFile : AppCompatActivity() {

    private var playBtn: ImageButton? = null
    private var pauseBtn: ImageButton? = null
    private val objVideoOnPreparedListener = VideoOnPreparedListener()
    private val objVideoOnCompletionListener = VideoOnCompletionListener()
    private val objPlayFileTouchListener = PlayFileTouchListener()
    private var isPaused: Boolean? = false
    private var subtitleBtn: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        CommonArgs.isPlaying = true
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) //hides notification bar
        setContentView(R.layout.activity_play_file)
        val playFileToolbar = findViewById<View>(R.id.player_toolbar) as Toolbar// for showing menu item
        setSupportActionBar(playFileToolbar)// for showing menu item
        supportActionBar!!.title = null // for showing menu item
        val bundle = intent.extras
        if (bundle != null) {
            CommonArgs.currentVideoPath = bundle.getString("fileName")
            CommonArgs.allVideoPath = bundle.getStringArrayList("allVideoPath")
        }
        val objPlayFileComponent = PlayFileComponentInit()
        CommonArgs.playFileCtx = this
        objPlayFileComponent.initPlayFileGlobal()
        objPlayFileComponent.initPlayFileLocal()
        playFile(CommonArgs.currentVideoPath)
        initListeners()
        initLocalVariable()
        playBtn!!.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.play_file_menu, menu)//Menu Resource, Menu
        subtitleBtn = menu.findItem(R.id.subtitles)
        subtitleBtn!!.isVisible = true
        subtitleBtn!!.title = "Subtitle Off"
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PlayBackResume().setResumePoint(CommonArgs.currentVideoPath, CommonArgs.videoView!!.currentPosition)
            CommonArgs.mediaPlayer!!.stop() // stops video playback
            CommonArgs.isPlaying = false
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    public override fun onDestroy() {
        CommonArgs.mediaPlayer!!.release() // releases associated resources
        super.onDestroy()
    }

    public override fun onPause() {
        CommonArgs.isPlaying = false
        PlayBackResume().setResumePoint(CommonArgs.currentVideoPath, CommonArgs.videoView!!.currentPosition)
        playBtn!!.visibility = View.VISIBLE
        pauseBtn!!.visibility = View.GONE
        super.onPause()
    }

    public override fun onResume() {
        if (!CommonArgs.isPlaying!!) {
            CommonArgs.setBackgroundOnResume = true
            playBtn!!.visibility = View.GONE
            pauseBtn!!.visibility = View.VISIBLE
        }
        super.onResume()
    }

    fun closeActivity() {
        CommonArgs.mediaPlayer!!.stop()
        finish()
    }

    private fun initLocalVariable() {
        playBtn = findViewById<View>(R.id.play_btn) as ImageButton
        pauseBtn = findViewById<View>(R.id.pause_btn) as ImageButton
    }

    private fun initListeners() {
        objVideoOnPreparedListener.totalTime = findViewById<View>(R.id.total_time) as TextView
        CommonArgs.videoView!!.setOnPreparedListener(objVideoOnPreparedListener)
        objVideoOnCompletionListener.ctx = this
        CommonArgs.videoView!!.setOnCompletionListener(objVideoOnCompletionListener)
        CommonArgs.rlPlayFile!!.setOnTouchListener(objPlayFileTouchListener)
    }

    fun playFile(filename: String?) {
        val vidTitle = findViewById<View>(R.id.vid_title) as TextView
        vidTitle.text = "Now Playing: " + File(filename).name
        CommonArgs.videoView!!.setVideoURI(Uri.parse(filename))
        Handler().postDelayed({ PlayBackResume().resumeFrom(CommonArgs.currentVideoPath) }, 100)
    }

    fun pauseVideo() {
        CommonArgs.isPlaying = false
        playBtn!!.visibility = View.VISIBLE
        pauseBtn!!.visibility = View.GONE
        CommonArgs.mediaPlayer!!.pause()
        CommonArgs.titleControlHandler.removeCallbacks(CommonArgs.titleControlRunnable) //showing view during pause
        isPaused = true
    }

    fun playVideo() {
        CommonArgs.isPlaying = true
        playBtn!!.visibility = View.GONE
        pauseBtn!!.visibility = View.VISIBLE
        CommonArgs.handler.postDelayed(CommonArgs.runnable, 10)
        CommonArgs.handler.postDelayed(CommonArgs.subtitleRunnable, 10)
        if ((!isPaused!!)) {
            PlayBackResume().resumePlayBackAuto(CommonArgs.currentVideoPath)
        }
        CommonArgs.mediaPlayer!!.start()
        PlayFileTouchListener().hideTitleControl() // hiding after 3 seconds of playback
        isPaused = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.subtitles -> {
                CommonArgs.handler.removeCallbacks(CommonArgs.subtitleRunnable)
                println("subtitle should be disabled")
                CommonArgs.subArea!!.text = ""
                subtitleBtn!!.title = "Subtitle On"
            }
        }
        return super.onOptionsItemSelected(item)
    }

}