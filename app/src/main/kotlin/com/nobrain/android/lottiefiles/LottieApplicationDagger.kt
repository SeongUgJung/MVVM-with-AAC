package com.nobrain.android.lottiefiles

import com.nobrain.android.lottiefiles.lottielist.dagger.LottieListComponent
import com.nobrain.android.lottiefiles.lottielist.dagger.LottieListModule
import com.nobrain.android.lottiefiles.repository.FilePath
import com.nobrain.android.lottiefiles.repository.api.LottieApiModule
import com.nobrain.android.lottiefiles.repository.local.database.LottieDaoModule
import com.nobrain.android.lottiefiles.repository.local.database.LottieDatabaseModule
import com.nobrain.android.lottiefiles.utils.ApplicationScope
import dagger.Component
import dagger.Module
import dagger.Provides


@ApplicationScope
@Component(modules = arrayOf(LottieApplicationModule::class,
    LottieApiModule::class,
    LottieDatabaseModule::class,
    LottieDaoModule::class
))
interface LottieApplicationComponent {
    fun inject(application: LottieApplication)
    fun lottieListComponent(lottieListModule: LottieListModule): LottieListComponent
}

@ApplicationScope
@Module
class LottieApplicationModule(private val application: LottieApplication) {
    @Provides fun application() = application
    @Provides fun filePath(): FilePath = object : FilePath {
        override fun path(): String {
            return "${application.filesDir.absolutePath}/lotties"
        }
    }
}