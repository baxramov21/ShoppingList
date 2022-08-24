package com.sheikh.shoppinglist.domain

class GetShoppingListUseCase(private val repository: Repository) {

    fun getShoppingList(): List<ShopItem> {
        return repository.getShoppingList()
    }
}