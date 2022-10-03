package com.sheikh.shoppinglist.domain.usecases

import com.sheikh.shoppinglist.domain.repository.Repository
import com.sheikh.shoppinglist.domain.items.ShopItem

class AddShopItemUseCase(private val repository: Repository) {

    fun addShopItem(item: ShopItem) {
        repository.addShopItem(item)
    }
}