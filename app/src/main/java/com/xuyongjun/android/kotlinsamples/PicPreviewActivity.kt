package com.xuyongjun.android.kotlinsamples

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.chrisbanes.photoview.PhotoView
import com.xuyongjun.android.kotlinsamples.consts.Const
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Created by admin on 2017/8/23.
 */
class PicPreviewActivity : AppCompatActivity() {

    lateinit var mImageUrl: String
    lateinit var mImageTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        initToolbar()
        initViews()

    }

    private fun initViews() {
        val mPhotoView = findViewById(R.id.photo_view) as PhotoView
        mImageUrl = intent.getStringExtra(Const.EXTRA_IMAGE_URL)
        mImageTitle = intent.getStringExtra(Const.EXTRA_IMAGE_TITLE)
        setTitle(mImageTitle)
        ViewCompat.setTransitionName(mPhotoView, Const.PIC_SHARED_ELEMENT)
        Glide.with(this)
                .load(mImageUrl)
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
        } else if (item?.itemId == R.id.action_save) {
            saveMeiziImage()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMeiziImage() {
        SaveImageUtils
                .saveImage(this, mImageUrl, mImageTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    val observer = object : Observer<String> {
        override fun onComplete() {
        }

        override fun onError(e: Throwable) {
            Toast.makeText(this@PicPreviewActivity, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(t: String) {
            Toast.makeText(this@PicPreviewActivity, t, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pic, menu)
        return super.onCreateOptionsMenu(menu)
    }
}