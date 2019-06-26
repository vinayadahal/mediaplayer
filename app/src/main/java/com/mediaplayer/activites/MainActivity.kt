package com.mediaplayer.activites

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import com.mediaplayer.R
import com.mediaplayer.components.MessageAlert
import com.mediaplayer.services.CleanUpService
import com.mediaplayer.services.IconService
import com.mediaplayer.services.MobileArrayAdapter
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {


    private val fileSize = ArrayList<Long>()
    private var toolbar: Toolbar? = null
    private var objMobileArrayAdapter: MobileArrayAdapter? = null
    private val ctx = this
    private var flagListCreated = 0

    val videoFiles: List<String>
        get() {
            val columns = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns._ID, MediaStore.MediaColumns.SIZE)
            val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.MediaColumns.TITLE)
            val videoFileList = ArrayList<String>()
            if (cursor!!.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    videoFileList.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)))
                    fileSize.add(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)))
                    cursor.moveToNext()
                }
            }
            cursor.close()
            return videoFileList
        }

   private val videoThumbnail: List<String>
        get() {
            val thumbLocation = ArrayList<String>()
            for (filepath in videoFiles) {
                val objIconService = IconService()
                objIconService.ctx = this
                thumbLocation.add(objIconService.checkThumb(filepath).toString())
            }
            return thumbLocation
        }

    private val videoDuration: List<Long>
        get() {
            val columns = arrayOf(MediaStore.Video.VideoColumns.DURATION)
            val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.MediaColumns.TITLE)
            val videoDuration = ArrayList<Long>()
            if (cursor!!.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val strDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
                    val duration = java.lang.Long.parseLong(strDuration)
                    videoDuration.add(duration)
                    cursor.moveToNext()
                }
            }
            cursor.close()
            return videoDuration
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        getPermission(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<View>(R.id.action_bar) as Toolbar// for showing menu item
        setSupportActionBar(toolbar)// for showing menu item
        supportActionBar!!.title = null // for showing menu item
        val backImgBtn = findViewById<View>(R.id.back_btn) as ImageButton
        backImgBtn.setOnClickListener { finish() }
        createVideoList(videoFiles, videoThumbnail, videoDuration, fileSize)
        cleanUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // should be define to attach menu item to action/toolbar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_item_activity, menu)
        val refreshBtn = menu.findItem(R.id.refresh_btn)
        refreshBtn.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // for handling menu item click
        when (item.itemId) {
            R.id.refresh_btn -> {
                println("refresh clicked")
                refreshIcons()
                MediaScannerConnection.scanFile(this@MainActivity,
                        arrayOf(Environment.getExternalStorageDirectory().toString()), null
                ) { path, _ ->
                    println("Scan Complete: $path")
                    println("Loading Video Files: ")
                    runOnUiThread {
                        cleanUp()
                        createVideoList(videoFiles, videoThumbnail, videoDuration, fileSize)
                    } // force reload of video file list
                } // refreshes file list
            }
        }
        return super.onOptionsItemSelected(item)
    }

   private fun createVideoList(videoFiles: List<String>, videoThumbnail: List<String>, duration: List<Long>, FileSize: List<Long>) {
        objMobileArrayAdapter = MobileArrayAdapter(this, videoFiles, videoThumbnail, duration, FileSize)
        val listView = ListView(this)
        listView.adapter = objMobileArrayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val fileName = videoFiles[position]
            if (checkFileExists(fileName)) { // checks if file exists or not before starting PlayFile activity.
                val intent = Intent(this@MainActivity, PlayFile::class.java)
                val bundle = Bundle()
                bundle.putString("fileName", fileName) // value for another activities
                bundle.putStringArrayList("allVideoPath", videoFiles as ArrayList<String>)
                intent.putExtras(bundle) // bundle saved as extras
                startActivity(intent)
            } else {
                MessageAlert().showToast("Can't play this file", ctx)
            }
        }
        val lnrlayout = findViewById<View>(R.id.lnrLayout) as LinearLayout
        lnrlayout.addView(listView)
        if (flagListCreated == 0) {
            flagListCreated = 1
        } else {
            lnrlayout.removeAllViews()
            lnrlayout.addView(listView)
        }
        refreshIcons()
    }

   private fun refreshIcons() {
        Handler().postDelayed({ objMobileArrayAdapter!!.notifyDataSetChanged() }, 5000)
    }

   private fun cleanUp() {
        val th = object : Thread() {
            override fun run() {
                CleanUpService.deleteTempPlayBack(videoFiles, ctx)
                CleanUpService.deleteThumbnail(videoFiles)
            }
        }
        th.start()
    }

  private  fun checkFileExists(fileName: String): Boolean {
        return File(fileName).exists()
    }

  private  fun getPermission(context: Context) {
        val storageReadPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Manifest.permission.READ_EXTERNAL_STORAGE
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN")
        }
      val storageWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionGranted = PackageManager.PERMISSION_GRANTED
        if (ContextCompat.checkSelfPermission(context, storageReadPermission) != permissionGranted) {
            ActivityCompat.requestPermissions(context as Activity,
                    arrayOf(storageReadPermission), 1)
        }
        if (ContextCompat.checkSelfPermission(context, storageWritePermission) != permissionGranted) {
            ActivityCompat.requestPermissions(context as Activity,
                    arrayOf(storageWritePermission), 1)
        }
    }
}