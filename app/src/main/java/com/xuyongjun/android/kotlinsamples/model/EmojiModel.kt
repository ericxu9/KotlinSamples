package com.xuyongjun.android.kotlinsamples.model

/**
 * Created by admin on 2017/8/23.
 */
class EmojiModel {
    var description: String? = null
    var image_url: String? = null

    override fun toString(): String = description + image_url
}