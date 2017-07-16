package com.nobrain.android.lottiefiles.repository

import android.arch.lifecycle.LiveData
import com.google.gson.Gson
import com.nobrain.android.lottiefiles.repository.api.FileDownloadApi
import com.nobrain.android.lottiefiles.repository.api.LottieApi
import com.nobrain.android.lottiefiles.repository.api.entities.Bodymovin
import com.nobrain.android.lottiefiles.repository.local.dao.LottieDao
import com.nobrain.android.lottiefiles.repository.local.entities.Lottie
import com.nobrain.android.lottiefiles.utils.subscribeIgnoreError
import com.nobrain.android.lottiefiles.utils.subscribeOnIO
import io.reactivex.Flowable
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject


class LottieModel @Inject constructor(private val lottieApi: LottieApi,
                                      private val lottieDao: LottieDao,
                                      private val downloadApi: FileDownloadApi,
                                      private val filePath: FilePath) {

    fun refresh() {
        getNew()
    }

    fun getRecentOnRx(): Flowable<List<Lottie>> {
        return lottieDao.getAllOnRx()
    }

    fun getRecentOnLiveData(): LiveData<List<Lottie>> {
        return lottieDao.getAll()
    }

    private fun getNew(page: Int = 1) {
        lottieApi.getRecent(page)
            .subscribeOnIO()
            .toObservable()
            .concatMap { Observable.fromIterable(it) }
            .filter { (id) -> lottieDao.getCount(id) <= 0 }
            .map { (id, dataUrl, title, author, authorProfile) ->
                var assetsPath: String? = null
                val path = filePath.path()
                File(path, id)
                    .apply {
                        if (!exists()) {
                            // {app-data}/files/lotties/{id}/
                            mkdirs()
                        }
                    }
                    .takeIf { it.list()?.isEmpty() ?: false }
                    ?.let {
                        // {app-data}/files/lotties/{id}/...
                        val itemPath = "$path/$id"
                        File(itemPath).takeIf { !it.exists() }?.mkdirs()
                        // {app-data}/files/lotties/{id}/{id}...
                        val jsonPath = "$itemPath/$id"
                        File(jsonPath).exists().takeIf { !it }?.let { downloadApi.downloadSync(dataUrl, jsonPath) }
                        Gson().fromJson(File(jsonPath).reader(), Bodymovin::class.java)
                            .assets
                            ?.filter { it.hasAssetFile() }
                            ?.forEach {
                                File(itemPath, it.u).apply {
                                    assetsPath = absolutePath
                                    if (!exists()) {
                                        mkdirs()
                                    }
                                }

                                val assetItemPath = "$itemPath/${it.u}/${it.p}"

                                if (!File(assetItemPath).exists()) {
                                    val assetItemUrl = "${dataUrl.substringBeforeLast("/")}/${it.u}/${it.p}"
                                    downloadApi.downloadSync(assetItemUrl, assetItemPath)
                                }

                            }
                    }

                Lottie(id, title, author, authorProfile, assetsPath)
                    .apply { lottieDao.insert(this) }
            }
            .collect({ ArrayList<Lottie>() }, { t1, t2 -> t1.add(t2) })
            .subscribeIgnoreError({ items ->
                if (items.size > 0) {
                    getNew(page + 1)
                }
            })
    }

    fun search(keyword: String) = lottieDao.query("%$keyword%")

}