package com.nobrain.android.lottiefiles.repository.api

import com.nobrain.android.lottiefiles.utils.observeOnMainThread
import com.nobrain.android.lottiefiles.utils.subscribeOnMainThread
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class FileDownloadApi @Inject constructor(private val retrofit: Retrofit) {

    fun downloadSync(url: String, filePath: String,
                     callback: (progress: Int) -> Unit = {},
                     done: (File) -> Unit = {},
                     error: (Throwable) -> Unit = {}) {

        retrofit.create(Api::class.java)
            .download(url)
            .doOnSuccess {
                Completable.fromAction { callback.invoke(0) }
                    .subscribeOnMainThread()
                    .subscribe()
            }
            .subscribe({ body ->
                val totalLength = body.contentLength()

                BufferedInputStream(body.byteStream()).use { inputStream ->
                    BufferedOutputStream(File(filePath).outputStream()).use { out ->
                        var bytesCopied: Long = 0
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var bytes = inputStream.read(buffer)
                        var lastProgress = -1
                        while (bytes >= 0) {
                            out.write(buffer, 0, bytes)
                            bytesCopied += bytes

                            totalLength.takeIf { it > 0 }?.let {
                                Single.just(bytesCopied.toDouble().div(it).times(100).toInt())
                            }?.filter { lastProgress != it }
                                ?.observeOnMainThread()
                                ?.subscribe { progressValue ->
                                    callback.invoke(progressValue)
                                    lastProgress = progressValue
                                }

                            bytes = inputStream.read(buffer)
                        }
                        out.flush()
                    }
                }

                Completable.fromAction { done.invoke(File(filePath)) }
                    .subscribeOnMainThread()
                    .subscribe()
            }, { t ->
                t.printStackTrace()
                Completable.fromAction { error.invoke(t) }
                    .subscribeOnMainThread()
                    .subscribe()
            })
    }

    internal interface Api {
        @GET
        @Streaming
        fun download(@Url url: String): Single<ResponseBody>
    }
}