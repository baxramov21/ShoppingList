package com.sheikh.shoppinglist.domain

class GetShopItem(private val repository: Repository) {

    fun getShopItem(id: Int) {
        repository.getShopItem(id)
    }
}