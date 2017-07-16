package com.nobrain.android.lottiefiles.repository.api

import common.rule.TestSchedulerRule
import dagger.Component
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.SingleScheduler
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility.await
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import javax.inject.Inject

class FileDownloadApiTest {
    @Inject lateinit var api: FileDownloadApi
    @get:Rule val rxRule = TestSchedulerRule()
    @Before
    fun setUp() {
        DaggerFileDownloadApiTest_LottieApiComponent.create()
            .inject(this@FileDownloadApiTest)
    }

    @Test
    fun downloadAsync() {
        var done = false
        var fail = false
        var progress = -1
        var file: File? = null

        api.downloadSync("http://www.lottiefiles.com/storage/datafiles/ALwCkdEwUEiFCTG/data.json",
            "./test.json",
            {
                progress = it
            },
            {
                done = true
                file = it
            },
            {
                fail = true
            })

        await().until { done or fail }

        Assertions.assertThat(progress).isGreaterThan(0)
        Assertions.assertThat(file!!).apply {
            exists()
            hasName("test.json")
        }
        file?.delete()

    }

    @Component(modules = arrayOf(LottieApiModule::class))
    interface LottieApiComponent {
        fun inject(test: FileDownloadApiTest)
    }
}
