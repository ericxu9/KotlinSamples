package com.xuyongjun.android.kotlinsamples.api

import com.xuyongjun.android.kotlinsamples.model.EmojiModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by admin on 2017/8/23.
 */
interface EmojiApi {

    @GET("search")
    fun getEmojiData(@Query("q") query: String) : Observable<List<EmojiModel>>

}