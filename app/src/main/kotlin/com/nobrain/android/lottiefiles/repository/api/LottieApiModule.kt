package com.nobrain.android.lottiefiles.repository.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.nobrain.android.lottiefiles.repository.api.converter.LottieConverterFactory
import com.nobrain.android.lottiefiles.utils.ApplicationScope
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@ApplicationScope
@Module
class LottieApiModule {
    @Provides
    internal fun okHttpClient() = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .addInterceptor(StethoInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply { level = Level.BASIC })
        .build()

    @Provides
    internal fun retrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(LottieConverterFactory())
        .client(okHttpClient)
        .baseUrl("https://www.lottiefiles.com/")
        .build()
}