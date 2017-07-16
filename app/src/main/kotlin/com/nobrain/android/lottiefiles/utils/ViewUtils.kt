package com.nobrain.android.lottiefiles.utils

import android.view.LayoutInflater
import android.view.View


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inflater(): LayoutInflater? = LayoutInflater.from(context)