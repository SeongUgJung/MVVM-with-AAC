package com.nobrain.android.lottiefiles.lottielist.dagger

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.nobrain.android.lottiefiles.lottielist.LottieListActivity
import com.nobrain.android.lottiefiles.lottielist.presenter.LottieListViewModel
import com.nobrain.android.lottiefiles.repository.LottieModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named


@Subcomponent(modules = arrayOf(LottieListModule::class))
interface LottieListComponent {
    fun inject(target: LottieListActivity)
}

@Module
class LottieListModule(private val lottieListActivity: LottieListActivity) {
    @Suppress("UNCHECKED_CAST")
    @Named("LottieListViewModel")
    @Provides fun viewModelProvider(lottieModel: LottieModel): ViewModelProvider =
        ViewModelProviders.of(lottieListActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LottieListViewModel(lottieModel, lottieListActivity) as T
            }
        })

    @Provides fun lottieListViewModel(@Named("LottieListViewModel") viewModelProvider: ViewModelProvider): LottieListViewModel =
        viewModelProvider.get(LottieListViewModel::class.java)

}