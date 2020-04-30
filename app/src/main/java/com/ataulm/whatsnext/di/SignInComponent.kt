package com.ataulm.whatsnext.di

import com.ataulm.whatsnext.account.SignInActivity
import dagger.Component

@Component(
        dependencies = [AppComponent::class]
)
@FeatureScope
interface SignInComponent {

    fun inject(activity: SignInActivity)

    @Component.Builder
    interface Builder {

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): SignInComponent
    }
}