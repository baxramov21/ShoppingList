package com.sheikh.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.sheikh.shoppinglist.domain.items.ShopItem

interface Repository {

    fun getShopItem(id: Int): ShopItem

    fun editShopItem(item: ShopItem)

    fun deleteShopItem(item: ShopItem)

    fun addShopItem(item: ShopItem)

    fun getShoppingList(): LiveData<List<ShopItem>>
}