package com.sheikh.shoppinglist.presentation.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.sheikh.shoppinglist.R
import com.sheikh.shoppinglist.domain.items.ShopItem
import com.sheikh.shoppinglist.presentation.view_model.ShopItemViewModel

class DetailScreenFragment : Fragment() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var nameInputError: MutableLiveData<Boolean>
    private lateinit var countInputError: MutableLiveData<Boolean>


    private var screenMode_value: String = UNDEFINED_SCREEN_MODE
    private var shopItemID_value: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_screen_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        when (screenMode_value) {
            MODE_ADD -> launchAddItemMode()
            MODE_EDIT -> launchEditItemMode()
        }
    }

    private fun launchEditItemMode() {
        viewModel.getItem(shopItemID_value)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            initET(it)
        }
    }

    private fun launchAddItemMode() {
       // Show Toast
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
        val args = requireArguments()
        if (!args.containsKey(PARAM_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is null")
        }

        val mode = args.getString(PARAM_SCREEN_MODE)

        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode")
        }

        // If everything is ok so set the value to variable
        screenMode_value = mode

        if (mode == MODE_EDIT) {
            if (!args.containsKey(PARAM_SHOP_ITEM_ID)) {
                throw RuntimeException("Unknown screen mode")
            }

            // If everything is ok so set the value to variable
            shopItemID_value = args.getInt(PARAM_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

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

    private fun onClickSaveItem() {
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
        if (screenMode_value == MODE_EDIT) {
            viewModel.editCurrentItem(itemNewName, itemNewCount)
            startMainActivity()
        } else if (screenMode_value == MODE_ADD) {
            viewModel.addNewItem(itemNewName, itemNewCount)
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        activity?.onBackPressed()
    }

    companion object {
        private const val PARAM_SCREEN_MODE = "extra_mode"
        private const val PARAM_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNDEFINED_SCREEN_MODE = ""

        fun newInstanceAddItem(): DetailScreenFragment {
            return DetailScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(ITEM_ID: Int): DetailScreenFragment {
            return DetailScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_SCREEN_MODE, MODE_EDIT)
                    putInt(PARAM_SHOP_ITEM_ID, ITEM_ID)
                }
            }
        }
    }
}