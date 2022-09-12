package com.sheikh.shoppinglist.domain

import androidx.lifecycle.LiveData

class GetShoppingListUseCase(private val repository: Repository) {

    fun getShoppingList(): LiveData<List<ShopItem>> {
        return repository.getShoppingList()
    }
}