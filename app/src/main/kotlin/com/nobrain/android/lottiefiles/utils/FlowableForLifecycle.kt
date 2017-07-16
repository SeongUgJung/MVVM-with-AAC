package com.nobrain.android.lottiefiles.utils

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction


object FlowableForLifecycle {
    fun <T> flowableForLifecycle(lifecycleOwner: LifecycleOwner, data: Flowable<T>, atLeast: Lifecycle.State = Lifecycle.State.RESUMED): Flowable<T> {
        return Flowable.combineLatest(Flowable.create<Lifecycle.State>({ e ->
            val observer: GenericLifecycleObserver = object : GenericLifecycleObserver {
                override fun getReceiver(): Any = lifecycleOwner

                override fun onStateChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
                    e.takeIf { !e.isCancelled }?.onNext(owner.lifecycle.currentState)
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        lifecycleOwner.lifecycle.removeObserver(this)
                    }
                }

            }
            lifecycleOwner.lifecycle.addObserver(observer)
            e.setCancellable { lifecycleOwner.lifecycle.removeObserver(observer) }
        }, BackpressureStrategy.LATEST),
            data,
            BiFunction<Lifecycle.State, T, T> { _, t2 -> t2 })
            .filter { lifecycleOwner.lifecycle.currentState.isAtLeast(atLeast) }
    }

}