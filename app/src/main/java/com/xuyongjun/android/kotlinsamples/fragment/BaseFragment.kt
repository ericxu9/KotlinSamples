package com.xuyongjun.android.kotlinsamples.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by admin on 2017/8/23.
 */
abstract class BaseFragment : Fragment() {

    /**
     * 视图是否加载完毕
     */
    var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    var hasLoadData = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(getLayoutId(),container,false)
        initViews(view)
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        lazyLoadDataIfPrepared()
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    abstract fun lazyLoad()

    abstract fun initViews(view: View?)

    @LayoutRes
    abstract fun getLayoutId():Int
}