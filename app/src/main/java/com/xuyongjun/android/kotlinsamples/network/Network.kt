package com.xuyongjun.android.kotlinsamples.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by admin on 2017/8/23.
 */
object Network {

    fun <T> configRetrofit(baseUrl: String, clazz: Class<T>): T {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(clazz)
    }
}