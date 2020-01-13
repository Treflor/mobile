package com.treflor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.treflor.data.db.TreflorDatabase
import com.treflor.data.db.datasources.*
import com.treflor.data.provider.JWTProvider
import com.treflor.data.provider.JWTProviderImpl
import com.treflor.data.provider.LocationProvider
import com.treflor.data.provider.LocationProviderImpl
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.datasources.*
import com.treflor.data.remote.intercepters.ConnectivityInterceptor
import com.treflor.data.remote.intercepters.ConnectivityInterceptorImpl
import com.treflor.data.remote.intercepters.UnauthorizedInterceptor
import com.treflor.data.remote.intercepters.UnauthorizedInterceptorImpl
import com.treflor.data.repository.Repository
import com.treflor.data.repository.RepositoryImpl
import com.treflor.ui.home.HomeViewModelFactory
import com.treflor.ui.journey.JourneyViewModelFactory
import com.treflor.ui.journey.start.StartJourneyViewModelFactory
import com.treflor.ui.login.LoginViewModelFactory
import com.treflor.ui.profile.ProfileViewModelFactory
import com.treflor.ui.signup.SignUpViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

const val CHANNEL_ID = "treflorServiceChannel"

class TreflorApplication : MultiDexApplication(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Treflor Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@TreflorApplication))

        // database
        bind() from singleton { TreflorDatabase(instance()) }
        bind() from singleton { instance<TreflorDatabase>().userDao() }
        bind() from singleton { instance<TreflorDatabase>().journeyDao() }
        bind() from singleton { instance<TreflorDatabase>().directionDao() }
        bind() from singleton { instance<TreflorDatabase>().trackedLocationsDao() }

        // providers
        bind<JWTProvider>() with singleton { JWTProviderImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance()) }

        // interceptors
        bind<ConnectivityInterceptor>() with singleton {
            ConnectivityInterceptorImpl(
                instance()
            )
        }
        bind<UnauthorizedInterceptor>() with singleton { UnauthorizedInterceptorImpl() }

        // api services
        bind() from singleton { TreflorApiService(instance()) }

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
        bind<TreflorGoogleServicesNetworkDataSource>() with singleton {
            TreflorGoogleServicesNetworkDataSourceImpl(
                instance(),
                instance()
            )
        }
        bind<JourneyNetworkDataSource>() with singleton {
            JourneyNetworkDataSourceImpl(
                instance(),
                instance()
            )
        }

        //data sources - database
        bind<UserDBDataSource>() with singleton { UserDBDataSourceImpl(instance()) }
        bind<JourneyDBDataSource>() with singleton { JourneyDBDataSourceImpl(instance()) }
        bind<DirectionDBDataSource>() with singleton { DirectionDBDataSourceImpl(instance()) }
        bind<TrackedLocationsDBDataSource>() with singleton {
            TrackedLocationsDBDataSourceImpl(
                instance()
            )
        }

        //repository
        bind<Repository>() with singleton {
            RepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        //view model factories
        bind() from provider { LoginViewModelFactory(instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from provider { SignUpViewModelFactory(instance()) }
        bind() from provider { JourneyViewModelFactory(instance(),instance()) }
        bind() from provider { StartJourneyViewModelFactory(instance(), instance()) }
    }

}