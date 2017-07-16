package com.nobrain.android.lottiefiles.repository.api

import com.nobrain.android.lottiefiles.repository.api.entities.LottieInfo
import com.nobrain.android.lottiefiles.utils.subscribeOnIO
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject


class LottieApi @Inject constructor(private val retrofit: Retrofit) {

    fun getRecent(page: Int = 1) = retrofit.create(Api::class.java).getRecent(page)
    fun getPopular(page: Int = 1) = retrofit.create(Api::class.java).getPopular(page)
    fun search(query: String) = retrofit.create(Api::class.java).search(query)

    internal interface Api {
        @GET("/")
        fun getRecent(@Query("page") page: Int): Single<List<LottieInfo>>

        @GET("/popular")
        fun getPopular(@Query("page") page: Int): Single<List<LottieInfo>>

        @GET("/search")
        fun search(@Query("q") query: String): Single<List<LottieInfo>>
    }
}