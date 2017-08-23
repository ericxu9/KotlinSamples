package com.xuyongjun.android.kotlinsamples.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xuyongjun.android.kotlinsamples.R
import com.xuyongjun.android.kotlinsamples.model.Item

/**
 * Created by admin on 2017/8/23.
 */
class CommonAdapter(var context: Context) : RecyclerView.Adapter<CommonAdapter.ViewHolder>() {

    var mItems: List<Item>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = mItems?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item: Item? = mItems?.get(position)
        Glide.with(context).load(item?.imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder?.ivImage)
        holder?.tvDesc?.text = item?.description
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_card_image, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivImage: ImageView? = null
        var tvDesc: TextView? = null


        init {
            ivImage = itemView.findViewById(R.id.iv_image)
            tvDesc = itemView.findViewById(R.id.tv_desc)
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    if (listener != null) {
                        listener?.onItemClick(position, mItems?.get(position)!!, p0!!)
                    }
                }

            })

        }

    }

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Item, view: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}