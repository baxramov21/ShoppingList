package com.sheikh.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sheikh.shoppinglist.domain.repository.Repository
import com.sheikh.shoppinglist.domain.items.ShopItem
import kotlin.random.Random

object RepositoryIml : Repository {

//    private val shoppingList = sortedSetOf(object : Comparator<ShopItem> {
//        override fun compare(p0: ShopItem?, p1: ShopItem?): Int {
//            var result: Int = 0
//            p0?.let {
//                val  firstItem = it
//                p1?.let {
//                    result = firstItem.ID.compareTo(it.ID)
//                }
//            }
//            return result
//        }
//    })

    private val shoppingList =
        sortedSetOf<ShopItem>({ object1, object2 -> object1.ID.compareTo(object2.ID) })
    private var autoIncrementID = 0
    private val shopListLV = MutableLiveData<List<ShopItem>>()

    init {
        for (i in 0 until 1000) {
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun getShopItem(id: Int): ShopItem {
        return shoppingList.find {
            it.ID == id
        } ?: throw RuntimeException("Element with $id ID not found")
    }

    override fun editShopItem(item: ShopItem) {
        val oldItem = getShopItem(item.ID)
        shoppingList.remove(oldItem)
        addShopItem(item)
    }

    override fun deleteShopItem(item: ShopItem) {
        shoppingList.remove(item)
        updateShopListLV()
    }

    override fun addShopItem(item: ShopItem) {
        if (item.ID == ShopItem.UNDEFINED_ID) {
            item.ID = autoIncrementID++
        }
        shoppingList.add(item)
        updateShopListLV()
    }

    override fun getShoppingList(): LiveData<List<ShopItem>> {
        return shopListLV
    }

    private fun updateShopListLV() {
        shopListLV.value = shoppingList.toList()
    }
}