package com.sheikh.shoppinglist.presentation.data_binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.sheikh.shoppinglist.R

@BindingAdapter("errorInputName")
fun bindNameInputError(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.in_name_error)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputCount")
fun bindCountInputError(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.in_count_error)
    } else {
        null
    }
    textInputLayout.error = message
}