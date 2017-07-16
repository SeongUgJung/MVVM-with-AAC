package com.nobrain.android.lottiefiles.lottielist.adpater

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nobrain.android.lottiefiles.databinding.ItemLottieListBinding
import com.nobrain.android.lottiefiles.repository.local.entities.Lottie
import com.nobrain.android.lottiefiles.utils.inflater

class LottieListAdapter(private val columnCount: Int) : RecyclerView.Adapter<LottieViewHolder>() {
    private val items = ArrayList<Lottie>()

    override fun onBindViewHolder(holder: LottieViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LottieViewHolder {
        val lottieItemBinding = ItemLottieListBinding.inflate(parent?.inflater(), parent, false)
        return LottieViewHolder(lottieItemBinding.apply {
            val columnWidth = root.resources.displayMetrics.let { Math.min(it.widthPixels, it.heightPixels) / columnCount }
            root.layoutParams = root.layoutParams.apply {
                width = columnWidth
                height = columnWidth
            }
        })
    }

    override fun getItemCount(): Int = items.size

    fun clear() {
        items.clear()
    }

    fun add(data: Lottie) {
        items.add(data)
    }

}


class LottieViewHolder(private val itemLottieListBinding: ItemLottieListBinding) : RecyclerView.ViewHolder(itemLottieListBinding.root) {

    fun bind(lottie: Lottie) {
        itemLottieListBinding.lottie = lottie
        itemLottieListBinding.executePendingBindings()

    }
}
