package com.diontambz.storyapp.custom_view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.diontambz.storyapp.R

class MyEmailEditText : AppCompatEditText {

    private lateinit var emailImage: Drawable
    private var isEmailValid: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        emailImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24) as Drawable
        onShowVisibilityIcon(emailImage)

        addTextChangedListener(onTextChanged = { _: CharSequence?, _: Int, _: Int, _: Int ->
            val email = text?.trim()
            if (email.isNullOrEmpty()) {
                isEmailValid = false
                error = resources.getString(R.string.input_email)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                isEmailValid = false
                error = resources.getString(R.string.invalid_email)
            } else {
                isEmailValid = true
            }
        })
    }

    private fun onShowVisibilityIcon(icon: Drawable) {
        setButtonDrawables(startOfTheText = icon)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }
}