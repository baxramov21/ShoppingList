package com.sheikh.shoppinglist.domain

class DeleteShopItemUseCase(private val repository: Repository) {

    fun deleteShopItem(item: ShopItem) {
        repository.deleteShopItem(item)
    }
}