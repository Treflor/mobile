package com.treflor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.treflor.data.db.TreflorDatabase
import com.treflor.data.provider.*
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.datasources.*
import com.treflor.data.remote.intercepters.ConnectivityInterceptor
import com.treflor.data.remote.intercepters.ConnectivityInterceptorImpl
import com.treflor.data.remote.intercepters.UnauthorizedInterceptor
import com.treflor.data.remote.intercepters.UnauthorizedInterceptorImpl
import com.treflor.data.repository.Repository
import com.treflor.data.repository.RepositoryImpl
import com.treflor.ui.home.HomeViewModelFactory
import com.treflor.ui.home.detailed.DetailedMapViewModelFactory
import com.treflor.ui.home.detailed.JourneyDetailsViewModelFactory
import com.treflor.ui.journey.JourneyViewModelFactory
import com.treflor.ui.journey.start.StartJourneyViewModelFactory
import com.treflor.ui.login.LoginViewModelFactory
import com.treflor.ui.profile.ProfileViewModelFactory
import com.treflor.ui.profile.journeys.UserJourneyViewModelFactory
import com.treflor.ui.settings.general.GeneralSettingsViewModelFactory
import com.treflor.ui.signup.SignUpViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

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
        bind() from singleton { instance<TreflorDatabase>().directionDao() }
        bind() from singleton { instance<TreflorDatabase>().trackedLocationsDao() }
        bind() from singleton { instance<TreflorDatabase>().journeyResponseDao() }

        // providers
        bind<JWTProvider>() with singleton { JWTProviderImpl(instance()) }
        bind<CurrentJourneyProvider>() with singleton { CurrentJourneyProviderImpl(instance()) }
        bind<CurrentUserProvider>() with singleton { CurrentUserProviderImpl(instance()) }
        bind<CurrentDirectionProvider>() with singleton { CurrentDirectionProviderImpl(instance()) }
        bind<LandmarkProvider>() with singleton { LandmarkProviderImpl(instance()) }
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
        bind() from provider { JourneyViewModelFactory(instance(), instance()) }
        bind() from provider { StartJourneyViewModelFactory(instance(), instance()) }
        bind() from factory { id: String -> JourneyDetailsViewModelFactory(instance(), id) }
        bind() from factory { id: String -> DetailedMapViewModelFactory(instance(), id) }
        bind() from provider { UserJourneyViewModelFactory(instance()) }
        bind() from provider { GeneralSettingsViewModelFactory(instance()) }
    }

}