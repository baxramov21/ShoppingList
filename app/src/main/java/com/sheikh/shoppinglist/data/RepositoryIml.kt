package com.sheikh.shoppinglist.data

import com.sheikh.shoppinglist.domain.Repository
import com.sheikh.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

object RepositoryIml : Repository {

    private val shoppingList = mutableListOf<ShopItem>()
    private var autoIncrementID = 0

    override fun getShopItem(id: Int): ShopItem {
        return shoppingList.find {
            it.ID == id
        } ?: throw RuntimeException("Element with $id ID not found")
    }

    override fun editShopItem(item: ShopItem) {
        val oldItem = getShopItem(item.ID)
        deleteShopItem(oldItem)
        addShopItem(item)
    }

    override fun deleteShopItem(item: ShopItem) {
        shoppingList.remove(item)
    }

    override fun addShopItem(item: ShopItem) {
        if (item.ID == ShopItem.UNDEFINED_ID) {
            item.ID = autoIncrementID++
            shoppingList.add(item)
        }
    }

    override fun getShoppingList(): List<ShopItem> {
        return shoppingList.toList()
    }
}