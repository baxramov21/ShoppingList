package com.sheikh.shoppinglist.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.sheikh.shoppinglist.R
import com.sheikh.shoppinglist.domain.items.ShopItem
import com.sheikh.shoppinglist.presentation.view_model.MainViewModel
import com.sheikh.shoppinglist.presentation.view_model.ShopItemViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var mainViewModel: MainViewModel

    private var shopItemID = ShopItem.UNDEFINED_ID
    private var screenMode = UNDEFINED_SCREEN_MODE
    private lateinit var itemName: String
    // staring value is -1 but it will change in process
    private var itemCount: Int = ShopItem.UNDEFINED_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        parseIntent()
        initViews()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        openScreenMode()
    }

    private fun openScreenMode() {
        when (screenMode) {
            MODE_ADD -> launchAddItemMode()
            MODE_EDIT -> launchEditItemMode()
        }
    }

    private fun launchEditItemMode() {
        viewModel.getItem(shopItemID)
        viewModel.shopItem.observe(this) {
            initET(it)
        }
    }

    private fun initET(shopItem: ShopItem) {
        with(shopItem) {
            etName.text = shopItemName.toEditable()
            etCount.text = shopItemCount.toString().toEditable()
            itemName = shopItemName
            itemCount = shopItemCount
        }
    }

    // Change String to Editable
    private fun String.toEditable(): Editable =
        Editable.Factory.getInstance().newEditable(this)

    private fun launchAddItemMode() {

    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is null")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)

        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Unknown screen mode")
            }
            shopItemID = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
            Log.d("id_is", shopItemID.toString())
        }
    }

    fun onClickSaveItem(view: View) {
        if (screenMode == MODE_EDIT) {
            viewModel.editCurrentItem(itemName, itemCount.toString())
            startMainActivity()
        } else {
            showToast("Error: Can't save item")
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val UNDEFINED_SCREEN_MODE = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemID: Int): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemID)
            return intent
        }
    }
}