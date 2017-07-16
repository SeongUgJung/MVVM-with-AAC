package com.nobrain.android.lottiefiles.lottielist.presenter

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.nobrain.android.lottiefiles.repository.LottieModel
import com.nobrain.android.lottiefiles.repository.local.entities.Lottie
import common.rule.TestSchedulerRule
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class LottieListViewModelTest {
    lateinit var viewModel: LottieListViewModel
    @Mock lateinit var lottieModel: LottieModel
    @Mock lateinit var lifecycleOwner: LifecycleOwner
    @Mock lateinit var lifecycle: Lifecycle
    @get:Rule val rxRule = TestSchedulerRule(false)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = LottieListViewModel(lottieModel, lifecycleOwner)
    }

    @Test
    fun onInit() {
        val flowable = Flowable.just(ArrayList<Lottie>().apply { add(Lottie(id = "1", assetsPath = "")) })
        Mockito.doReturn(flowable).`when`(lottieModel).getRecentOnRx()
        Mockito.doReturn(lifecycle).`when`(lifecycleOwner).lifecycle
        Mockito.doAnswer {
            Mockito.doReturn(Lifecycle.State.RESUMED).`when`(lifecycle).currentState
            (it.arguments[0] as GenericLifecycleObserver).onStateChanged(lifecycleOwner, Lifecycle.Event.ON_RESUME)
            it
        }.`when`(lifecycle).addObserver(ArgumentMatchers.any())

        viewModel.onInit()

        (rxRule.scheduler as TestScheduler).advanceTimeTo(1001, TimeUnit.MILLISECONDS)

        Mockito.verify(lottieModel).getRecentOnRx()
        Assertions.assertThat(viewModel.lotties)
            .isNotEmpty()
            .hasSize(1)
            .extracting("id", String::class.java)
            .contains("1")

    }
}