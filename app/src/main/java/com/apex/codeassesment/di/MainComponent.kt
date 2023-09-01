package com.apex.codeassesment.di

import android.content.Context
import com.apex.codeassesment.ui.details.DetailsActivityKt
import com.apex.codeassesment.ui.location.LocationActivity
import com.apex.codeassesment.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [MainModule::class])
interface MainComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance applicationContext: Context): MainComponent
  }

  interface Injector {
    val mainComponent: MainComponent
  }

  fun inject(mainActivity: MainActivity)
  fun inject(detailsActivityKt: DetailsActivityKt)
  fun inject(locationActivity: LocationActivity)
}