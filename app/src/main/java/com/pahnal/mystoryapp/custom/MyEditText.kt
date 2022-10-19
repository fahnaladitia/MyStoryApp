package com.pahnal.mystoryapp.custom

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.pahnal.mystoryapp.R

class MyEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun afterTextChanged(s: Editable) {
                if (inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    showValidationPassword(s)
                } else if (inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                    showValidationEmail(s)
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        })
    }

     fun showValidationEmail(char: CharSequence) {
        error = if (android.util.Patterns.EMAIL_ADDRESS.matcher(char)
                .matches() || char.isEmpty()
        ) null else context.resources.getString(R.string.email_validation_error)

    }

    private fun showValidationPassword(char: CharSequence) {
        error =
            if (char.length > 5 || char.isEmpty()) null else context.resources.getString(R.string.password_validation_error)

    }

}