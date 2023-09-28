package com.ataulm.whatsnext.nav

interface NavigateToSearch {
    operator fun invoke()

    companion object {
        operator fun invoke(impl: () -> Unit): NavigateToSearch {
            return object : NavigateToSearch {
                override fun invoke() {
                    impl()
                }
            }
        }
    }
}
