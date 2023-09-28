package com.ataulm.whatsnext.di;

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.film.FilmActivity
import com.ataulm.whatsnext.film.FilmViewModel
import com.ataulm.whatsnext.film.FilmViewModelProviderFactory
import com.ataulm.whatsnext.model.FilmSummary
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object FilmModule {

    @JvmStatic
    @Provides
    fun activity(@ActivityContext context: Context): FilmActivity {
        return context as FilmActivity
    }

    @JvmStatic
    @Provides
    fun filmSummary(activity: FilmActivity): FilmSummary {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotNull(
                activity.intent.getSerializableExtra(
                    FilmActivity.EXTRA_FILM_SUMMARY,
                    FilmSummary::class.java
                )
            ) {
                "how you open FilmActivity without a film id?"
            }
        } else {
            @Suppress("DEPRECATION")
            checkNotNull(activity.intent.getSerializableExtra(FilmActivity.EXTRA_FILM_SUMMARY) as? FilmSummary) {
                "how you open FilmActivity without a film id?"
            }
        }
    }

    @JvmStatic
    @Provides
    fun viewModel(activity: FilmActivity, factory: FilmViewModelProviderFactory) =
        ViewModelProvider(activity, factory)[FilmViewModel::class.java]
}
