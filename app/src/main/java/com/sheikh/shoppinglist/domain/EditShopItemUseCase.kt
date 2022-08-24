package com.sheikh.shoppinglist.domain

class EditShopItemUseCase(private val repository: Repository) {

    fun editShopItem(item: ShopItem) {
        repository.editShopItem(item)
    }
}