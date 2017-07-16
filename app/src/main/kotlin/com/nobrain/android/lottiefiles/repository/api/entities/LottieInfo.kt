package com.nobrain.android.lottiefiles.repository.api.entities


data class LottieInfo(val id: String,
                      val dataUrl: String,
                      val title: String? = null,
                      val author: String? = null,
                      val authorProfile: String? = null)