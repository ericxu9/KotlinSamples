package com.xuyongjun.android.kotlinsamples

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.xuyongjun.android.kotlinsamples.fragment.EmojiFragment
import com.xuyongjun.android.kotlinsamples.fragment.MeiziFragment

class MainActivity : AppCompatActivity(),TabLayout.OnTabSelectedListener {


    lateinit var mViewPager: ViewPager
    lateinit var mTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.viewPager) as ViewPager
        mTabLayout = findViewById(R.id.tabLayout) as TabLayout

        initTabLayout()
    }

    private fun initTabLayout() {
        val tabs: Array<String> = resources.getStringArray(R.array.tabs)
        tabs.forEach { mTabLayout.addTab(mTabLayout.newTab()) }
        val tabsAdapter = TabsAdapter(supportFragmentManager,tabs)
        mTabLayout.setupWithViewPager(mViewPager)
        mViewPager.adapter = tabsAdapter
        mTabLayout.addOnTabSelectedListener(this@MainActivity)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
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
