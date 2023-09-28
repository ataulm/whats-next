package com.ataulm.whatsnext.di

import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.UserIsSignedInUseCase
import com.ataulm.whatsnext.account.SignInUseCase
import com.ataulm.whatsnext.search.SearchActivity
import com.ataulm.whatsnext.search.SearchViewModel
import com.ataulm.whatsnext.search.SearchViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

//@Component(
//        dependencies = [AppComponent::class],
//        modules = [
//            SearchModule::class
//        ]
//)
//@FeatureScope
//interface SearchComponent {
//
//    fun inject(activity: SearchActivity)
//
//    @Component.Builder
//    interface Builder {
//
//        @BindsInstance
//        fun activity(activity: SearchActivity): Builder
//
//        fun appComponent(appComponent: AppComponent): Builder
//
//        fun build(): SearchComponent
//    }
//}

@Module
@InstallIn(ActivityComponent::class)
object SearchModule {

    @JvmStatic
    @Provides
    fun viewModel(
        activity: SearchActivity,
        userIsSignedInUseCase: UserIsSignedInUseCase,
        signInUseCase: SignInUseCase,
        whatsNextRepository: WhatsNextRepository
    ): SearchViewModel {
        val viewModelFactory = SearchViewModelFactory(
                userIsSignedInUseCase,
                signInUseCase,
                whatsNextRepository
        )
        return ViewModelProvider(activity, viewModelFactory).get(SearchViewModel::class.java)
    }
}
