package com.treflor

import android.app.Application
import com.treflor.data.provider.JWTProvider
import com.treflor.data.provider.JWTProviderImpl
import com.treflor.ui.login.LoginViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class TreflorApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@TreflorApplication))

        bind<JWTProvider>() with singleton { JWTProviderImpl(instance()) }
        bind() from provider { LoginViewModelFactory(instance()) }
    }

}