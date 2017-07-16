package com.nobrain.android.lottiefiles.anim

import com.airbnb.lottie.LottieComposition
import java.lang.ref.WeakReference
import java.util.*


class LottieAnimCache {
    private val weakRefCache = HashMap<String, WeakReference<LottieComposition>>()

    fun getComposition(name: String) = weakRefCache[name]
    fun putComposition(name: String, composition: LottieComposition) {
        if (weakRefCache.get(name) != composition) {
            weakRefCache.put(name, WeakReference(composition))
        }
    }

    companion object {
        val cacheStrategy = LottieAnimCache()
    }
}

class LottieAnimStatus {

    private val status = HashMap<String, Status>()

    fun isInProgress(name: String) = status[name]?.let { it == Status.IN_PROGRESS } ?: false
    fun updateDone(name: String) {
        status[name] = Status.DONE
    }

    fun updateInProgress(name: String) {
        status[name] = Status.IN_PROGRESS
    }

    enum class Status {
        IN_PROGRESS, DONE
    }

    companion object {
        val status = LottieAnimStatus()
    }
}