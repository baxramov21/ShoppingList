package com.sheikh.shoppinglist.domain.usecases

import com.sheikh.shoppinglist.domain.repository.Repository
import com.sheikh.shoppinglist.domain.items.ShopItem

class EditShopItemUseCase(private val repository: Repository) {

    fun editShopItem(item: ShopItem) {
        repository.editShopItem(item)
    }
}