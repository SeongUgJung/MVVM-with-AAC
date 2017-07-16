package com.nobrain.android.lottiefiles.utils


fun <X, Y, Z> safeLet(x: X?, y: Y?, call: (X, Y) -> Z): Z? {

    return x?.let { x1 ->
        y?.let { y1 ->
            call.invoke(x1, y1)
        }
    }
}