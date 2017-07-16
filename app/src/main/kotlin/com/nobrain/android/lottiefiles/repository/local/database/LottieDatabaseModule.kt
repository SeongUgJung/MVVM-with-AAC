package com.nobrain.android.lottiefiles.repository.local.database

import android.arch.persistence.room.Room
import com.nobrain.android.lottiefiles.LottieApplication
import com.nobrain.android.lottiefiles.utils.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class LottieDatabaseModule {
    @Provides
    @ApplicationScope
    fun database(app: LottieApplication) =
        Room.databaseBuilder(app, LottieDatabase::class.java, "lottie").build()

}

@Module
class LottieDaoModule {
    @Provides
    fun lottieDao(database: LottieDatabase) = database.getLottieDao()
}