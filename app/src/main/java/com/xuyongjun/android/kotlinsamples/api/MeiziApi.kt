package com.xuyongjun.android.kotlinsamples.api

import com.xuyongjun.android.kotlinsamples.model.MeiziResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by admin on 2017/8/23.
 */
interface MeiziApi {
    @GET("data/福利/{pageSize}/{pageCount}")
    fun getMeiziDataByPage(@Path("pageSize") pageSize: Int, @Path("pageCount") pageCount: Int): Observable<MeiziResult>

}