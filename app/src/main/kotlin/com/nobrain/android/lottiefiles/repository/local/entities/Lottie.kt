package com.nobrain.android.lottiefiles.repository.local.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class Lottie(@PrimaryKey var id: String,
                  var title: String? = null,
                  var author: String? = null,
                  var authorProfile: String? = null,
                  var assetsPath:String?,
                  var starred:Boolean = false)