package com.sheikh.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.sheikh.shoppinglist.domain.items.ShopItem

interface Repository {

    suspend fun getShopItem(id: Int): ShopItem

    suspend fun editShopItem(item: ShopItem)

    suspend fun deleteShopItem(item: ShopItem)

    suspend fun addShopItem(item: ShopItem)

    fun getShoppingList(): LiveData<List<ShopItem>>
}