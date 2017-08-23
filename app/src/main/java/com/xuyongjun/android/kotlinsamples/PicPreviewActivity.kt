package com.xuyongjun.android.kotlinsamples

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.chrisbanes.photoview.PhotoView
import com.xuyongjun.android.kotlinsamples.consts.Const

/**
 * Created by admin on 2017/8/23.
 */
class PicPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        initToolbar()
        initViews()

    }

    private fun initViews() {
        val mPhotoView = findViewById(R.id.photo_view) as PhotoView
        val imageUrl = intent.getStringExtra(Const.EXTRA_IMAGE_URL)
        val title = intent.getStringExtra(Const.EXTRA_IMAGE_TITLE)
        setTitle(title)
        ViewCompat.setTransitionName(mPhotoView, Const.PIC_SHARED_ELEMENT)
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(mPhotoView)
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }else if (item?.itemId == R.id.action_save) {

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pic, menu)
        return super.onCreateOptionsMenu(menu)
    }
}