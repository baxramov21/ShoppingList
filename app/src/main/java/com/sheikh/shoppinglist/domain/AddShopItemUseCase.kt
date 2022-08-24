package com.sheikh.shoppinglist.domain

class AddShopItemUseCase(private val repository: Repository) {

    fun addShopItem(item: ShopItem) {
        repository.addShopItem(item)
    }
}