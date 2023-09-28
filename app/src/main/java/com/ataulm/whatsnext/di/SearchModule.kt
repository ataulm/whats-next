package com.ataulm.whatsnext.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.SignInUseCase
import com.ataulm.whatsnext.account.UserIsSignedInUseCase
import com.ataulm.whatsnext.search.SearchActivity
import com.ataulm.whatsnext.search.SearchViewModel
import com.ataulm.whatsnext.search.SearchViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object SearchModule {

    @JvmStatic
    @Provides
    fun activity(@ActivityContext context: Context): SearchActivity {
        return context as SearchActivity
    }

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
