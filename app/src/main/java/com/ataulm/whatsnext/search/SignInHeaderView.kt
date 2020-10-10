package com.ataulm.whatsnext.search

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.ataulm.support.exhaustive
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.merge_sign_in_header.view.*

class SignInHeaderView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_sign_in_header, this)
        signInHeaderUsernameEditText.addTextChangedListener { updateButtonEnabled() }
        signInHeaderPasswordEditText.addTextChangedListener { updateButtonEnabled() }
    }

    private fun updateButtonEnabled() {
        val validUsername = !signInHeaderUsernameEditText.text.isNullOrBlank()
        val validPassword = !signInHeaderPasswordEditText.text.isNullOrBlank()
        signInHeaderButton.isEnabled = validUsername && validPassword
    }

    fun update(uiModel: UiModel) {
        when (uiModel) {
            UiModel.Loading -> showLoading()
            is UiModel.Idle -> {
                signInHeaderButton.setOnClickListener {
                    val username = signInHeaderUsernameEditText.text.toString()
                    val password = signInHeaderPasswordEditText.text.toString()
                    uiModel.onClickSignIn(username, password)
                }
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
    }

    sealed class UiModel {
        object Loading : UiModel()
        data class Idle(
                val onClickSignIn: (String, String) -> Unit,
                val onClickRegister: () -> Unit,
                val errorMessage: String?
        ) : UiModel()
    }
}
