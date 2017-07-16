package com.nobrain.android.lottiefiles.lottielist.presenter

import android.arch.lifecycle.*
import android.databinding.ObservableArrayList
import com.nobrain.android.lottiefiles.repository.LottieModel
import com.nobrain.android.lottiefiles.repository.local.entities.Lottie
import com.nobrain.android.lottiefiles.utils.FlowableForLifecycle
import com.nobrain.android.lottiefiles.utils.observeOnMainThread
import com.nobrain.android.lottiefiles.utils.subscribeIgnoreError
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LottieListViewModel @Inject constructor(private val lottieModel: LottieModel,
                                              lifecycleOwner: LifecycleOwner) : ViewModel(), LifecycleObserver {
    val lotties = ObservableArrayList<Lottie>()
    val querySubject = BehaviorProcessor.createDefault("")!!
    val lottiesDisposable = querySubject.subscribeOn(Schedulers.computation())
        .switchMap { query ->
            if (query.isNullOrBlank()) {
                FlowableForLifecycle.flowableForLifecycle(lifecycleOwner, lottieModel.getRecentOnRx())
            } else {
                FlowableForLifecycle.flowableForLifecycle(lifecycleOwner, lottieModel.search(query))
            }
        }
        .distinctUntilChanged()
        .sample(1, TimeUnit.SECONDS, true)
        .observeOnMainThread()
        .subscribeIgnoreError({ lotties ->
            this.lotties.clear()
            this.lotties.addAll(lotties)
        })

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onInit() {
        lottieModel.refresh()

    }

    fun onSearch(text: CharSequence) {
        querySubject.onNext(text.toString())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lottiesDisposable.takeIf { !it.isDisposed }?.dispose()
    }

}