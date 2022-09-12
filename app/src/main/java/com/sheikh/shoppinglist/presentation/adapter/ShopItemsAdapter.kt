package com.sheikh.shoppinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sheikh.shoppinglist.R
import com.sheikh.shoppinglist.domain.ShopItem

class ShopItemsAdapter : ListAdapter<ShopItem, ShopItemsViewHolder>(ShopItemDiffCallback()) {

    var onShopIteClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemsViewHolder {
        val itemStateLayout = when (viewType) {
            itemDisabled -> R.layout.item_disabled
            itemEnabled -> R.layout.item_enabled
            else -> throw RuntimeException("Unknown view type")
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(
                itemStateLayout,
                parent,
                false
            )
        return ShopItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemsViewHolder, position: Int) {
        val shopItem = getItem(position)
        with(holder) {
            with(shopItem) {
                tvItemName.text = shopItemName
                tvItemCount.text = shopItemCount.toString()
            }
            itemView.setOnClickListener {
                onShopIteClickListener?.invoke(shopItem)
            }

            itemView.setOnLongClickListener {
                onShopItemLongClickListener?.invoke(shopItem)
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        return if (shopItem.isShopItemEnabled) {
            itemEnabled
        } else {
            itemDisabled
        }
    }

    companion object {
        const val itemEnabled = 100
        const val itemDisabled = 200
        const val disabledPoolCount = 15
        const val enabledPoolCount = 15
    }
}