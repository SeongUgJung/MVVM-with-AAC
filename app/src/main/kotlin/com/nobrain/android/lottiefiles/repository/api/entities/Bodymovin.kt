package com.nobrain.android.lottiefiles.repository.api.entities


data class Bodymovin(var assets: List<LottieAsset>?)

data class LottieAsset(var id: String,
                       var p: String?,
                       var u: String?) {
    fun hasAssetFile(): Boolean = !p.isNullOrBlank()
}