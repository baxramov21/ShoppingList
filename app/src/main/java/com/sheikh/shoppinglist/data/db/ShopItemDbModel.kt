package com.sheikh.shoppinglist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
class ShopItemDbModel (
    @PrimaryKey(autoGenerate = true)
    val ID: Int,
    val shopItemName: String,
    val shopItemCount: Int,
    var isShopItemEnabled: Boolean
)