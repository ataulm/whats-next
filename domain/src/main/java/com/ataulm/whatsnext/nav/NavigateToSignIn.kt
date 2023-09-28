package com.ataulm.whatsnext.nav

interface NavigateToSignIn {
    operator fun invoke()

    companion object {
        operator fun invoke(impl: () -> Unit): NavigateToSignIn {
            return object : NavigateToSignIn {
                override fun invoke() {
                    impl()
                }
            }
        }
    }
}
