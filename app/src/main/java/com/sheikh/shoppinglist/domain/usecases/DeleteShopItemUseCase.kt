package com.sheikh.shoppinglist.domain.usecases

import com.sheikh.shoppinglist.domain.repository.Repository
import com.sheikh.shoppinglist.domain.items.ShopItem

class DeleteShopItemUseCase(private val repository: Repository) {

    suspend fun deleteShopItem(item: ShopItem) {
        repository.deleteShopItem(item)
    }
}