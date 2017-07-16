package com.nobrain.android.lottiefiles.lottielist

import android.arch.lifecycle.LifecycleActivity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.nobrain.android.lottiefiles.LottieApplication
import com.nobrain.android.lottiefiles.R
import com.nobrain.android.lottiefiles.databinding.ActLottieListBinding
import com.nobrain.android.lottiefiles.lottielist.adpater.LottieListAdapter
import com.nobrain.android.lottiefiles.lottielist.dagger.LottieListModule
import com.nobrain.android.lottiefiles.lottielist.presenter.LottieListViewModel
import kotlinx.android.synthetic.main.act_lottie_list.*
import javax.inject.Inject


class LottieListActivity : LifecycleActivity() {
    @Inject lateinit var lottieListViewModel: LottieListViewModel
    val databinding: ActLottieListBinding by lazy {
        DataBindingUtil.setContentView<ActLottieListBinding>(this, R.layout.act_lottie_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as LottieApplication)
            .appComponent
            .lottieListComponent(LottieListModule(this))
            .inject(this)
        databinding.viewmodel = lottieListViewModel

        rv_lottie_list.adapter = LottieListAdapter(COLUMN_COUNT)
        rv_lottie_list.layoutManager = GridLayoutManager(this, COLUMN_COUNT)
        lifecycle.addObserver(lottieListViewModel)
    }

    companion object {
        private val COLUMN_COUNT = 3

    }
}