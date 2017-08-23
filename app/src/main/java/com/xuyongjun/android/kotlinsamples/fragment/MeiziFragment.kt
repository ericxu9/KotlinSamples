package com.xuyongjun.android.kotlinsamples.fragment

import android.content.Intent
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.xuyongjun.android.kotlinsamples.*
import com.xuyongjun.android.kotlinsamples.adapter.CommonAdapter
import com.xuyongjun.android.kotlinsamples.api.MeiziApi
import com.xuyongjun.android.kotlinsamples.consts.Const
import com.xuyongjun.android.kotlinsamples.model.Item
import com.xuyongjun.android.kotlinsamples.network.Network
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by admin on 2017/8/22.
 */
class MeiziFragment : BaseFragment(), CommonAdapter.OnItemClickListener {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mRefreshLayout: SwipeRefreshLayout
    lateinit var mDisposable: Disposable
    lateinit var mAdapter: CommonAdapter
    lateinit var mLayoutManager: GridLayoutManager
    var lastVisibleItem: Int = 0
    var mCurrentPage: Int = 1
    var mListData: ArrayList<Item> = ArrayList()

    override fun lazyLoad() {
        loadPage(1, false)
    }

    private fun loadPage(pageCount: Int, isLoadMore: Boolean) {
        if (!isLoadMore)
            mRefreshLayout.isRefreshing = true
        val meiziApi = Network.configRetrofit(Const.API_MEIZI, MeiziApi::class.java)
        meiziApi
                .getMeiziDataByPage(20, pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { t ->
                    val items: ArrayList<Item> = ArrayList()
                    t.results?.forEach {
                        val item = Item()
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.CHINA)
                        val outputFormat = SimpleDateFormat("yy/MM/dd HH:mm:ss", Locale.CHINA)
                        val date = inputFormat.parse(it.createdAt)
                        item.description = outputFormat.format(date)
                        item.imageUrl = it.url
                        items.add(item)
                    }
                    items
                }
                .subscribe(object : Observer<List<Item>> {
                    override fun onSubscribe(d: Disposable) {
                        mDisposable = d
                    }

                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable) {
                        mRefreshLayout.isRefreshing = false
                        Toast.makeText(context, getString(R.string.load_data_error), Toast.LENGTH_LONG).show()
                    }

                    override fun onNext(items: List<Item>) {
                        if (isLoadMore) {
                            mCurrentPage++
                        }
                        mRefreshLayout.isRefreshing = false
                        if (items.isEmpty()) Toast.makeText(context, getString(R.string.no_found_data), Toast.LENGTH_SHORT).show()
                        mListData.addAll(items)
                        mAdapter.mItems = mListData
                    }

                })
    }

    override fun initViews(view: View?) {
        mAdapter = CommonAdapter(context)
        mAdapter.listener = this
        mRecyclerView = view?.findViewById(R.id.recyclerView)!!
        mLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.adapter = mAdapter


        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isSlideToBottom(recyclerView)) {
                    loadPage(mCurrentPage + 1, true)
                }
            }
        })

        mRefreshLayout = view.findViewById(R.id.refreshLayout)
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE)
        mRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            mListData.clear()
            loadPage(1, false)
        }

    }

    override fun onItemClick(position: Int, item: Item, view: View) {
        val intent = Intent(context, PicPreviewActivity::class.java)
        intent.putExtra(Const.EXTRA_IMAGE_URL, item.imageUrl)
        intent.putExtra(Const.EXTRA_IMAGE_TITLE, item.description)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, view, Const.PIC_SHARED_ELEMENT)
        ActivityCompat.startActivity(context, intent, optionsCompat.toBundle())
    }


    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }


    override fun getLayoutId(): Int = R.layout.fragment_common

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable.isDisposed) {
            mDisposable.dispose()
        }
    }

}