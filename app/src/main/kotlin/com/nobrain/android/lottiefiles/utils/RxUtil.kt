package com.nobrain.android.lottiefiles.utils

import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


fun Completable.subscribeOnMainThread(): Completable {
    this.subscribeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
    return this
}

fun <T> Single<T>.subscribeOnMainThread(): Single<T> {
    this.subscribeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
    return this
}

fun <T> Single<T>.observeOnMainThread(): Single<T> {
    return observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.subscribeIgnoreError(onNext: (T) -> Unit, onComplete: () -> Unit = {}): Disposable {
    return subscribe(onNext, {}, onComplete)
}

fun <T> Flowable<T>.observeOnMainThread(): Flowable<T> {
    return observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
}
fun <T> Flowable<T>.subscribeIgnoreError(onNext: (T) -> Unit, onComplete: () -> Unit = {}): Disposable {
    return subscribe(onNext, {it.printStackTrace()}, onComplete)
}

fun <T> Maybe<T>.observeOnMainThread(): Maybe<T> {
    return observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subscribeIgnoreError(onSuccess: (t: T) -> Unit): Disposable {
    return subscribe(onSuccess, {it.printStackTrace()})
}

fun <T> Single<T>.subscribeOnIO(): Single<T> {
    return subscribeOn(Schedulers.io())
}