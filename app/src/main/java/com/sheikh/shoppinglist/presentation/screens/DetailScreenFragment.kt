package com.sheikh.shoppinglist.presentation.screens

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.sheikh.shoppinglist.databinding.DetailScreenFragmentBinding
import com.sheikh.shoppinglist.domain.items.ShopItem
import com.sheikh.shoppinglist.presentation.screens.interfaces.OnEditingFinishedListener
import com.sheikh.shoppinglist.presentation.view_model.ShopItemViewModel

class DetailScreenFragment : Fragment() {

    private var _binding: DetailScreenFragmentBinding? = null
    private val binding: DetailScreenFragmentBinding
        get() = _binding ?: throw RuntimeException("DetailScreenFragmentBinding is null")

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var nameInputError: MutableLiveData<Boolean>
    private lateinit var countInputError: MutableLiveData<Boolean>

    private var screenModeValue: String = UNDEFINED_SCREEN_MODE
    private var shopItemIdValue: Int = ShopItem.UNDEFINED_ID

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Interface OnEditingFinishedListener must be realized")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailScreenFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        launchScreenMode()
        inNoErrorInValues(binding.etName)
        inNoErrorInValues(binding.etCount)
        nameInputError = viewModel.errorInputName as MutableLiveData<Boolean>
        countInputError = viewModel.errorInputCount as MutableLiveData<Boolean>

        viewModel.canFinishScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
            activity?.onBackPressed()
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
            resetInputErrors()
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
        when (screenModeValue) {
            MODE_ADD -> launchAddItemMode()
            MODE_EDIT -> launchEditItemMode()
        }
    }

    private fun launchEditItemMode() {
        viewModel.getItem(shopItemIdValue)
        binding.buttonSave.setOnClickListener {
            viewModel.editCurrentItem(getNewName(), getNewCount())
        }
    }

    private fun launchAddItemMode() {
        binding.buttonSave.setOnClickListener {
            viewModel.addNewItem(getNewName(), getNewCount())
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
        screenModeValue = mode

        if (mode == MODE_EDIT) {
            if (!args.containsKey(PARAM_SHOP_ITEM_ID)) {
                throw RuntimeException("Unknown screen mode")
            }

            // If everything is ok so set the value to variable
            shopItemIdValue = args.getInt(PARAM_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun getNewName(): String = binding.etName.text.toString()

    private fun getNewCount(): String = binding.etCount.text.toString()

    companion object {
        private const val PARAM_SCREEN_MODE = "extra_mode"
        private const val PARAM_SHOP_ITEM_ID = "extra_shop_item_id"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
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