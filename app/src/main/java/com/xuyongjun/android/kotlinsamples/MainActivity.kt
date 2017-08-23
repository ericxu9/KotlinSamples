package com.xuyongjun.android.kotlinsamples

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.xuyongjun.android.kotlinsamples.fragment.EmojiFragment
import com.xuyongjun.android.kotlinsamples.fragment.MeiziFragment

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mViewPager: ViewPager
    lateinit var mTabLayout: TabLayout
    lateinit var mFloatingBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.viewPager) as ViewPager
        mTabLayout = findViewById(R.id.tabLayout) as TabLayout
        mFloatingBtn = findViewById(R.id.floating_btn) as FloatingActionButton

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
}
