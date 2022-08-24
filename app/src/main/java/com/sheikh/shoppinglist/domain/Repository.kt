package com.sheikh.shoppinglist.domain

interface Repository {

    fun getShopItem(id: Int)

    fun editShopItem(item: ShopItem): ShopItem

    fun deleteShopItem(item: ShopItem)

    fun addShopItem(item: ShopItem)

    fun getShoppingList(): List<ShopItem>
}