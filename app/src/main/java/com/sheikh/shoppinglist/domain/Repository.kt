package com.sheikh.shoppinglist.domain

interface Repository {

    fun getShopItem(id: Int): ShopItem

    fun editShopItem(item: ShopItem)

    fun deleteShopItem(item: ShopItem)

    fun addShopItem(item: ShopItem)

    fun getShoppingList(): List<ShopItem>
}