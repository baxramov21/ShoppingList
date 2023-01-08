package com.sheikh.shoppinglist.presentation.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.sheikh.shoppinglist.data.RepositoryIml
import com.sheikh.shoppinglist.domain.usecases.DeleteShopItemUseCase
import com.sheikh.shoppinglist.domain.usecases.EditShopItemUseCase
import com.sheikh.shoppinglist.domain.usecases.GetShoppingListUseCase
import com.sheikh.shoppinglist.domain.items.ShopItem

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryIml(application)

    private val deleteShopItemUseCase =  DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShoppingListUseCase = GetShoppingListUseCase(repository)

    val shoppingList = getShoppingListUseCase.getShoppingList()

    fun deleteShopItem(item: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(item)
    }

    fun changeEnabledState(item: ShopItem) {
        val copy = item.copy(isShopItemEnabled = !item.isShopItemEnabled)
        editShopItemUseCase.editShopItem(copy)
    }
}