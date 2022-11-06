package com.sheikh.shoppinglist.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.sheikh.shoppinglist.R
import com.sheikh.shoppinglist.domain.items.ShopItem
import com.sheikh.shoppinglist.presentation.view_model.ShopItemViewModel

class DetailScreenFragment(
    private val screenMode: String = UNDEFINED_SCREEN_MODE,
    private val shopItemID: Int = ShopItem.UNDEFINED_ID
) : Fragment() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var nameInputError: MutableLiveData<Boolean>
    private lateinit var countInputError: MutableLiveData<Boolean>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_screen_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        initViews(view)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        launchScreenMode()
        inNoErrorInValues(etName)
        inNoErrorInValues(etCount)
        nameInputError = viewModel.errorInputName as MutableLiveData<Boolean>
        countInputError = viewModel.errorInputCount as MutableLiveData<Boolean>

        buttonSave.setOnClickListener {
            onClickSaveItem()
        }
    }


    private fun resetInputErrors() {
        with(viewModel) {
            resetErrorInputName()
            resetErrorInputCount()
        }
    }

    private fun inNoErrorInValues(editText: EditText) {
        addTextChangedListener(editText) {
            noErrorInValues()
        }
    }

    private fun addTextChangedListener(editText: EditText, onTextChanged: () -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun launchScreenMode() {
        when (screenMode) {
            MODE_ADD -> launchAddItemMode()
            MODE_EDIT -> launchEditItemMode()
        }
    }

    private fun launchEditItemMode() {
        viewModel.getItem(shopItemID)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            initET(it)
        }
    }

    private fun launchAddItemMode() {
//        showToast("Create new item")
    }

    private fun initET(shopItem: ShopItem) {
        with(shopItem) {
            etName.text = shopItemName.toEditable()
            etCount.text = shopItemCount.toString().toEditable()
        }
    }

    // Change String to Editable
    private fun String.toEditable(): Editable =
        Editable.Factory.getInstance().newEditable(this)

    private fun initViews(view: View) {
        with(view) {
            tilName = findViewById(R.id.til_name)
            tilCount = findViewById(R.id.til_count)
            etName = findViewById(R.id.et_name)
            etCount = findViewById(R.id.et_count)
            buttonSave = findViewById(R.id.buttonSave)
        }
    }

    private fun parseParams() {
      if (screenMode != MODE_ADD && screenMode != MODE_EDIT) {
          throw RuntimeException("Param screen mode is null")
      }
        if (screenMode == MODE_EDIT && shopItemID == ShopItem.UNDEFINED_ID) {
            throw RuntimeException("No item found with this ID")
        }
    }

    /*
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
     */

    private fun onErrorInValues() {
        tilName.error = "Name or count is wrong "
        tilCount.error = "Name or count is wrong "
        nameInputError.value = true
        countInputError.value = true
    }

    private fun noErrorInValues() {
        tilName.error = null
        tilCount.error = null
        resetInputErrors()
    }

    private fun getNewName(): String = etName.text.toString()

    private fun getNewCount(): String = etCount.text.toString()

    private fun nameInputError(): Boolean {
        var errorInName = true
        nameInputError.observe(viewLifecycleOwner) {
            errorInName = it
        }
        return errorInName
    }

    private fun countInputError(): Boolean {
        var errorInCount = true
        countInputError.observe(viewLifecycleOwner) {
            errorInCount = it
        }
        return errorInCount
    }

    fun onClickSaveItem() {
        val itemNewName = getNewName()
        val itemNewCount = getNewCount()

        if (!nameInputError() && !countInputError()) {
            noErrorInValues()
            chooseMode(itemNewName, itemNewCount)
        } else {
            onErrorInValues()
        }
    }

    private fun chooseMode(itemNewName: String, itemNewCount: String) {
        if (screenMode == MODE_EDIT) {
            viewModel.editCurrentItem(itemNewName, itemNewCount)
            startMainActivity()
        } else if (screenMode == MODE_ADD) {
//            showToast("Add new item")
            viewModel.addNewItem(itemNewName, itemNewCount)
            startMainActivity()
        }
    }

    private fun startMainActivity() {
//        startActivity(Intent(this, MainActivity::class.java))
        activity?.onBackPressed()
    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val UNDEFINED_SCREEN_MODE = ""

        fun newInstanceAddItem(): DetailScreenFragment {
            return DetailScreenFragment(MODE_ADD)
        }

        fun newInstanceEditItem(ITEM_ID: Int): DetailScreenFragment {
            return DetailScreenFragment(MODE_EDIT, ITEM_ID)
        }

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