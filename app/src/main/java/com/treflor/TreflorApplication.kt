package com.treflor

import android.app.Application
import com.treflor.data.db.TreflorDatabase
import com.treflor.data.db.datasources.UserDBDataSource
import com.treflor.data.db.datasources.UserDBDataSourceImpl
import com.treflor.data.provider.JWTProvider
import com.treflor.data.provider.JWTProviderImpl
import com.treflor.data.remote.api.TreflorAuthApiService
import com.treflor.data.remote.api.TreflorUserApiService
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSource
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSourceImpl
import com.treflor.data.remote.datasources.UserNetworkDataSource
import com.treflor.data.remote.datasources.UserNetworkDataSourceImpl
import com.treflor.data.remote.intercepters.ConnectivityInterceptor
import com.treflor.data.remote.intercepters.ConnectivityInterceptorImpl
import com.treflor.data.remote.intercepters.UnauthorizedInterceptor
import com.treflor.data.remote.intercepters.UnauthorizedInterceptorImpl
import com.treflor.data.repository.Repository
import com.treflor.data.repository.RepositoryImpl
import com.treflor.ui.home.HomeViewModelFactory
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

        // database
        bind() from singleton { TreflorDatabase(instance()) }
        bind() from singleton { instance<TreflorDatabase>().userDao() }

        // providers
        bind<JWTProvider>() with singleton { JWTProviderImpl(instance()) }

        // interceptors
        bind<ConnectivityInterceptor>() with singleton {
            ConnectivityInterceptorImpl(
                instance()
            )
        }
        bind<UnauthorizedInterceptor>() with singleton { UnauthorizedInterceptorImpl() }

        // api services
        bind() from singleton { TreflorAuthApiService(instance()) }
        bind() from singleton { TreflorUserApiService(instance()) }

        //data sources - network
        bind<AuthenticationNetworkDataSource>() with singleton {
            AuthenticationNetworkDataSourceImpl(
                instance()
            )
        }
        bind<UserNetworkDataSource>() with singleton {
            UserNetworkDataSourceImpl(
                instance(),
                instance()
            )
        }

        //data sources - database
        bind<UserDBDataSource>() with singleton { UserDBDataSourceImpl(instance()) }

        //repository
        bind<Repository>() with singleton {
            RepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        //view model factories
        bind() from provider { LoginViewModelFactory(instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
    }

}