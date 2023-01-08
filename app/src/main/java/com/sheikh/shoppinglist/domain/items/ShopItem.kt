package com.sheikh.shoppinglist.domain.items

data class ShopItem(
    val shopItemName: String,
    val shopItemCount: Int,
    var isShopItemEnabled: Boolean,
    var ID: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
