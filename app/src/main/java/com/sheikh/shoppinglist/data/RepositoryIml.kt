package com.sheikh.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.sheikh.shoppinglist.data.db.AppDatabase
import com.sheikh.shoppinglist.domain.items.ShopItem
import com.sheikh.shoppinglist.domain.repository.Repository

class RepositoryIml(
    application: Application
) : Repository {

    private val shopItemDao = AppDatabase.getInstance(application).getDao()
    private val mapper = Mapper()

    override fun getShopItem(id: Int): ShopItem {
        val dbModel = shopItemDao.getShopItem(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun editShopItem(item: ShopItem) {
        shopItemDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override fun deleteShopItem(item: ShopItem) {
        shopItemDao.deleteShopItem(item.ID)
    }

    override fun addShopItem(item: ShopItem) {
        shopItemDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override fun getShoppingList(): LiveData<List<ShopItem>> =
        MediatorLiveData<List<ShopItem>>().apply {
            addSource(shopItemDao.getShopItemsList()) {
                value = mapper.mapListDbModelToListEntity(it)
            }
        }
}