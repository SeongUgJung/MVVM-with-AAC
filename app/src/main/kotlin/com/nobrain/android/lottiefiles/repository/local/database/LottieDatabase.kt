package com.nobrain.android.lottiefiles.repository.local.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.nobrain.android.lottiefiles.repository.local.dao.LottieDao
import com.nobrain.android.lottiefiles.repository.local.entities.Lottie


@Database(entities = arrayOf(Lottie::class), version = 1)
abstract class LottieDatabase : RoomDatabase() {
    abstract fun getLottieDao(): LottieDao
}