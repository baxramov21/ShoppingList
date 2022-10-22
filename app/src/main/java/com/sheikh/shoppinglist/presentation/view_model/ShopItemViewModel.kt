package com.sheikh.shoppinglist.presentation.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sheikh.shoppinglist.data.RepositoryIml
import com.sheikh.shoppinglist.domain.usecases.AddShopItemUseCase
import com.sheikh.shoppinglist.domain.usecases.EditShopItemUseCase
import com.sheikh.shoppinglist.domain.items.GetShopItem
import com.sheikh.shoppinglist.domain.items.ShopItem
import java.lang.Exception

class ShopItemViewModel : ViewModel() {

    private val repository = RepositoryIml

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _canFinishScreen = MutableLiveData<Unit>()
    val canFinishScreen: LiveData<Unit>
        get() = _canFinishScreen

    private val getItemByID = GetShopItem(repository)
    private val editItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    fun editCurrentItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isInputsValid = validateInputs(name, count)
        Log.d("inputsValid",isInputsValid.toString())
        if (isInputsValid) {
            _shopItem.value?.let {
                val copiedItem = it.copy(shopItemName = name, shopItemCount = count)
                editItemUseCase.editShopItem(copiedItem)
                Log.d("itemIS", it.toString())
                closeScreen()
            }
        }
    }

    fun addNewItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isInputsValid = validateInputs(name, count)
        if (isInputsValid) {
            val item = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(item)
            closeScreen()
        }
    }

    fun getItem(ID: Int) {
        val item = getItemByID.getShopItem(ID)
        _shopItem.value = item
    }

    fun resetErrorInputName() {
        _errorInputName.value = false // this was false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false   // this was false
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return try {
            count?.toInt() ?: 0
        } catch (error: Exception) {
            0
        }
    }

    private fun validateInputs(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }

        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    private fun closeScreen() {
        _canFinishScreen.value = Unit
    }
}