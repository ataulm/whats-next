package com.ataulm.letterboxd

import android.app.Application
import dagger.BindsInstance
import dagger.Component

//@LetterboxdScope
@Component(
    modules = [
//        LetterboxdModule::class
    ]
)
interface LetterboxdComponent {

//    val letterboxdRepository: LetterboxdRepository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun with(application: Application): Builder
        fun build(): LetterboxdComponent
    }
}
