package com.ataulm.whatsnext.search

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.ataulm.support.exhaustive
import com.ataulm.whatsnext.R

class SignInHeaderView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var signInHeaderUsernameEditText: EditText
    private lateinit var signInHeaderPasswordEditText: EditText
    private lateinit var signInHeaderButton: Button
    private lateinit var signInHeaderRegisterButton: Button

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_sign_in_header, this)
        signInHeaderUsernameEditText = findViewById(R.id.signInHeaderUsernameEditText)
        signInHeaderPasswordEditText = findViewById(R.id.signInHeaderPasswordEditText)
        signInHeaderButton = findViewById(R.id.signInHeaderButton)
        signInHeaderRegisterButton = findViewById(R.id.signInHeaderRegisterButton)
        signInHeaderUsernameEditText.addTextChangedListener { updateSignInButtonEnabled() }
        signInHeaderPasswordEditText.addTextChangedListener { updateSignInButtonEnabled() }
    }

    private fun updateSignInButtonEnabled() {
        val validUsername = !signInHeaderUsernameEditText.text.isNullOrBlank()
        val validPassword = !signInHeaderPasswordEditText.text.isNullOrBlank()
        signInHeaderButton.isEnabled = validUsername && validPassword
    }

    fun update(uiModel: UiModel) {
        when (uiModel) {
            UiModel.Loading -> showLoading()
            is UiModel.RequiresSignIn -> {
                // TODO: show error message

                updateSignInButtonEnabled()
                signInHeaderButton.setOnClickListener {
                    val username = signInHeaderUsernameEditText.text.toString()
                    val password = signInHeaderPasswordEditText.text.toString()
                    uiModel.onClickSignIn(username, password)
                }

                signInHeaderRegisterButton.isEnabled = true
                signInHeaderRegisterButton.setOnClickListener { uiModel.onClickRegister() }
            }
        }.exhaustive
    }

    private fun showLoading() {
        // TODO: would be neat if this was a ProgressButton instead, so that it shows an
        //  indeterminate loading spinner instead of the text while loading:
        //  ```
        //  signInHeaderButton.isLoading = true
        //  ```
        signInHeaderButton.isEnabled = false

        // we're only disabling this because the click listener is not part of the Loading class
        signInHeaderRegisterButton.isEnabled = false
    }

    sealed class UiModel {
        object Loading : UiModel()
        data class RequiresSignIn(
            val onClickSignIn: (String, String) -> Unit,
            val onClickRegister: () -> Unit,
            val errorMessage: String?
        ) : UiModel()
    }
}
