package com.nobrain.android.lottiefiles.repository.api

import dagger.Component
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class LottieApiTest {
    @Inject lateinit var api: LottieApi
    @Before
    fun setUp() {
        DaggerLottieApiTest_LottieApiComponent.create()
            .inject(this@LottieApiTest)
    }

    @Test
    fun getRecent() {
        api.getRecent()
            .test()
            .apply { awaitTerminalEvent() }
            .assertValueCount(1)
            .assertNoErrors()
            .values()
            .apply {
                assertThat(this.size).isGreaterThan(0)
            }

    }

    @Test
    fun getPopular() {
        api.getPopular()
            .test()
            .apply { awaitTerminalEvent() }
            .assertValueCount(1)
            .assertNoErrors()

    }

    @Test
    fun search() {
        api.search("star")
            .test()
            .apply { awaitTerminalEvent() }
            .assertValueCount(1)
            .assertNoErrors()

    }

    @Component(modules = arrayOf(LottieApiModule::class))
    interface LottieApiComponent {
        fun inject(test: LottieApiTest)
    }
}
