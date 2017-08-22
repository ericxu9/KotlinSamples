package com.xuyongjun.android.kotlinsamples.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xuyongjun.android.kotlinsamples.R

/**
 * Created by admin on 2017/8/22.
 */

class EmojiFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_emoji,container,false)
}
