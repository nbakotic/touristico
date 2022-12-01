package com.example.touristico.utils;

import android.R
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class InputValidator(context: Context) {
    private val context: Context
    fun isInputEditTextFilled(
        textInputEditText: TextInputEditText,
        textInputLayout: TextInputLayout
    ): Boolean {
        val message: String = "This field id mandatory"
        val value = textInputEditText.text.toString().trim { it <= ' ' }
        if (value.isEmpty()) {
            textInputLayout.error = message
            hideKeyboardFrom(textInputEditText)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    fun isInputEditTextFilled(
        autoCompleteTextView: AutoCompleteTextView,
        textInputLayout: TextInputLayout
    ): Boolean {
        val message: String = "This field id mandatory"
        val value = autoCompleteTextView.text.toString().trim { it <= ' ' }
        if (value.isEmpty()) {
            textInputLayout.error = message
            //hideKeyboardFrom(autoCompleteTextView);
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun hideKeyboardFrom(view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            view.getWindowToken(),
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    init {
        this.context = context
    }
}