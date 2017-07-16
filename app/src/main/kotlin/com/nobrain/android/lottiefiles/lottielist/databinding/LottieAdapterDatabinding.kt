package com.nobrain.android.lottiefiles.lottielist.databinding

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.nobrain.android.lottiefiles.anim.LottieAnimCache
import com.nobrain.android.lottiefiles.anim.LottieAnimStatus
import com.nobrain.android.lottiefiles.lottielist.adpater.LottieListAdapter
import com.nobrain.android.lottiefiles.repository.local.entities.Lottie
import java.io.File


object LottieAdapterDatabinding {

    @JvmStatic
    @BindingAdapter("lottieitems")
    fun bindLottieDatas(view: RecyclerView, items: List<Lottie>?) {
        if (view.adapter is LottieListAdapter) {
            val lottieAdapterModel: LottieListAdapter = view.adapter as LottieListAdapter

            lottieAdapterModel.clear()
            items?.forEach {
                lottieAdapterModel.add(it)
            }

            view.adapter.notifyDataSetChanged()
        }
    }

    @JvmStatic
    @BindingAdapter("lottieAnim")
    fun bindLottieAnim(animView: LottieAnimationView, lottie: Lottie) {
        animView.tag = lottie.id
        lottie.assetsPath?.let {
            animView.setImageAssetDelegate {
                val filePath = "${lottie.assetsPath}/${it.fileName}"
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(filePath, options)

                val rate: Int = options.outWidth / it.width

                val sample = (rate.takeIf { it > 0 }?.div(2)?.times(2)?.plus(1)) ?: 1
                options.inSampleSize = sample
                options.inJustDecodeBounds = false


                val origin: Bitmap? = BitmapFactory.decodeFile(filePath, options)
                origin?.takeIf { origin.width != it.width }?.let {
                    Bitmap.createScaledBitmap(origin, it.width, it.height, false)
                        .apply {
                            origin.recycle()
                        }
                } ?: origin
            }

        }
        LottieAnimCache.cacheStrategy.getComposition(lottie.id)?.get()?.let {
            if (animView.tag == lottie.id) {
                animView.setComposition(it)
                animView.playAnimation()
            }
        } ?: if (!LottieAnimStatus.status.isInProgress(lottie.id)) {
            LottieAnimStatus.status.updateInProgress(lottie.id)
            LottieComposition.Factory.fromInputStream(animView.context, File("${animView.context.filesDir.absoluteFile}/lotties/${lottie.id}/${lottie.id}")
                .inputStream(), { composition ->
                composition?.let { it ->
                    if (animView.tag == lottie.id) {
                        animView.setComposition(it)
                        animView.playAnimation()
                    }
                    LottieAnimCache.cacheStrategy.putComposition(lottie.id, it)
                    LottieAnimStatus.status.updateDone(lottie.id)
                }
            })
        }
    }
}