package com.sheikh.shoppinglist.data

import com.sheikh.shoppinglist.data.db.ShopItemDbModel
import com.sheikh.shoppinglist.domain.items.ShopItem

class Mapper {

    fun mapEntityToDbModel(shopItem: ShopItem): ShopItemDbModel = ShopItemDbModel(
        ID = shopItem.ID,
        shopItemName = shopItem.shopItemName,
        shopItemCount = shopItem.shopItemCount,
        isShopItemEnabled = shopItem.isShopItemEnabled
    )

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel): ShopItem = ShopItem(
        ID = shopItemDbModel.ID,
        shopItemName = shopItemDbModel.shopItemName,
        shopItemCount = shopItemDbModel.shopItemCount,
        isShopItemEnabled = shopItemDbModel.isShopItemEnabled
    )

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}