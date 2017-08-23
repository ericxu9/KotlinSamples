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
import com.xuyongjun.android.kotlinsamples.api.EmojiApi
import com.xuyongjun.android.kotlinsamples.consts.Const
import com.xuyongjun.android.kotlinsamples.model.Item
import com.xuyongjun.android.kotlinsamples.network.Network
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by admin on 2017/8/22.
 */

class EmojiFragment : BaseFragment(), CommonAdapter.OnItemClickListener {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mRefreshLayout: SwipeRefreshLayout
    lateinit var mDisposable: Disposable
    lateinit var mAdapter: CommonAdapter
    var mDefaultQuery: String = "可爱"


    override fun lazyLoad() {
        search(mDefaultQuery)
    }

    public fun search(str: String) {
        mRefreshLayout.isRefreshing = true
        val emojiApi: EmojiApi = Network.configRetrofit(Const.API_EMOJI, EmojiApi::class.java)
        mDefaultQuery = str
        emojiApi
                .getEmojiData(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { t ->
                    val items: ArrayList<Item> = ArrayList()
                    t.forEach {
                        val item = Item()
                        item.description = it.description
                        item.imageUrl = it.image_url
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
                        if (items.isEmpty()) Toast.makeText(context, getString(R.string.no_found_data), Toast.LENGTH_SHORT).show()
                        mAdapter.mItems = items
                        mRefreshLayout.isRefreshing = false
                    }
                })
    }

    override fun initViews(view: View?) {
        mAdapter = CommonAdapter(context)
        mAdapter.listener = this
        mRecyclerView = view?.findViewById(R.id.recyclerView)!!
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.adapter = mAdapter

        mRefreshLayout = view.findViewById(R.id.refreshLayout)
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE)

        mRefreshLayout.setOnRefreshListener {
            search(mDefaultQuery)
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

    override fun getLayoutId(): Int = R.layout.fragment_common

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable.isDisposed) {
            mDisposable.dispose()
        }
    }
}
