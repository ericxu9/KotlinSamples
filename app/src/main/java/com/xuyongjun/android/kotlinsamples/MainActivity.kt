package com.xuyongjun.android.kotlinsamples

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.xuyongjun.android.kotlinsamples.fragment.EmojiFragment
import com.xuyongjun.android.kotlinsamples.fragment.MeiziFragment
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mViewPager: ViewPager
    lateinit var mTabLayout: TabLayout
    lateinit var mFloatingBtn: FloatingActionButton

    val REQUEST_PERMISSION_CODE: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.viewPager) as ViewPager
        mTabLayout = findViewById(R.id.tabLayout) as TabLayout
        mFloatingBtn = findViewById(R.id.floating_btn) as FloatingActionButton

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                val array = Array<String>(1, init = { android.Manifest.permission.WRITE_EXTERNAL_STORAGE })
                ActivityCompat.requestPermissions(this,array, REQUEST_PERMISSION_CODE)
            }
        }

        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        val tabs: Array<String> = resources.getStringArray(R.array.tabs)
        tabs.forEach { mTabLayout.addTab(mTabLayout.newTab()) }
        val tabsAdapter = TabsAdapter(supportFragmentManager, tabs)
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.adapter = tabsAdapter
        mViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                hiddenFloatingButton(position)
            }

        })

        mFloatingBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val view = LayoutInflater.from(this).inflate(R.layout.search_view, null, false) as EditText
        val build = AlertDialog.Builder(this)
        build.setTitle("表情包")
        build.setView(view)
        build.setPositiveButton("搜索") { p0, _ ->
            (supportFragmentManager.fragments[0] as EmojiFragment).search(view.text.toString())
            p0?.dismiss()
        }
        build.setNegativeButton("取消") { p0, _ -> p0?.dismiss() }
        build.create().show()
    }


    private fun hiddenFloatingButton(position: Int) {
        if (position == 0) {
            mFloatingBtn.visibility = View.VISIBLE
        } else {
            mFloatingBtn.visibility = View.GONE
        }
    }

    class TabsAdapter(fm: FragmentManager?, var tabs: Array<String>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position == 0)
                EmojiFragment()
            else
                MeiziFragment()
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence = tabs[position]

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(this@MainActivity, "拒绝权限无法保存照片哦！", Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}
