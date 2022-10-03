package com.sheikh.shoppinglist.domain.usecases

import androidx.lifecycle.LiveData
import com.sheikh.shoppinglist.domain.repository.Repository
import com.sheikh.shoppinglist.domain.items.ShopItem

class GetShoppingListUseCase(private val repository: Repository) {

    fun getShoppingList(): LiveData<List<ShopItem>> {
        return repository.getShoppingList()
    }
}