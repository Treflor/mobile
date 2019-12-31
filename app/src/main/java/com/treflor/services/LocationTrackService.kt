package com.treflor.services

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.treflor.CHANNEL_ID
import com.treflor.data.repository.Repository
import com.treflor.internal.LocationUpdateReciever
import com.treflor.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class LocationTrackService : LifecycleService(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val repository: Repository by instance()

    private val TAG = "LocationTrackService"

    override fun onCreate() {
        Log.e(TAG, "Location Track service created!")
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("")
                .setContentIntent(pendingIntent)
                .build()

            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "Location service started!")

        repository.requestLocationUpdate(LocationUpdateReciever.LOCATION_SERVICE).observe(this@LocationTrackService,
                Observer {
                    // should persist locations
                    Log.d("location","location: ${it.latitude} ${it.longitude}")
                })


        return super.onStartCommand(intent, flags, startId);
    }

    override fun onDestroy() {
        repository.removeLocationUpdate(LocationUpdateReciever.LOCATION_SERVICE)
        super.onDestroy()
    }

}