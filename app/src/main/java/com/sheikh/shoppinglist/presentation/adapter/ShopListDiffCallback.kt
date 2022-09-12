package com.sheikh.shoppinglist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sheikh.shoppinglist.domain.ShopItem

class ShopListDiffCallback
    (
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getItemIDByPosition(oldItemPosition, oldList).ID ==
                getItemIDByPosition(newItemPosition, newList).ID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getItemIDByPosition(oldItemPosition, oldList) ==
                getItemIDByPosition(newItemPosition, newList)
    }

    private fun getItemIDByPosition(position: Int, listOfItem: List<ShopItem>): ShopItem {
        return listOfItem[position]
    }
}