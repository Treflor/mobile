package com.treflor.data.provider

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.treflor.models.Journey
import com.treflor.models.TreflorPlace

const val JOURNEY_TITLE = "journey_title"
const val JOURNEY_CONTENT = "journey_content"
const val JOURNEY_ORIGIN = "journey_origin"
const val JOURNEY_DESTINATION = "journey_destination"
const val JOURNEY_LEVEL = "journey_level"
const val JOURNEY_LABELS = "journey_labels"

class CurrentJourneyProviderImpl(context: Context) : PreferenceProvider(context),
    CurrentJourneyProvider {
    override val currentJourney: LiveData<Journey>
        get() = _currentJourney
    private val _currentJourney by lazy { MutableLiveData<Journey>(getCurrentJourney()) }

    override fun getCurrentJourney(): Journey? {
        val title = preferences.getString(JOURNEY_TITLE, "")
        val content = preferences.getString(JOURNEY_CONTENT, "")
        val origin =
            Gson().fromJson(preferences.getString(JOURNEY_ORIGIN, ""), TreflorPlace::class.java)
        val destination = Gson().fromJson(
            preferences.getString(JOURNEY_DESTINATION, ""),
            TreflorPlace::class.java
        )
        val level = preferences.getString(JOURNEY_LEVEL, "")
        val labels = preferences.getStringSet(JOURNEY_LABELS, setOf())
        return if (!title.isNullOrEmpty()) Journey(
            "",
            title,
            content!!,
            origin,
            destination,
            level!!,
            labels!!.toList()
        ) else null
    }

    override fun persistCurrentJourney(journey: Journey) {
        val editor = preferences.edit()
        editor.putString(JOURNEY_TITLE, journey.title)
        editor.putString(JOURNEY_CONTENT, journey.content)
        editor.putString(JOURNEY_ORIGIN, Gson().toJson(journey.origin))
        editor.putString(JOURNEY_DESTINATION, Gson().toJson(journey.destination))
        editor.putString(JOURNEY_LEVEL, journey.level)
        editor.putStringSet(JOURNEY_LABELS, journey.labels.toSet())
        editor.apply()
        _currentJourney.postValue(journey)
    }

    override fun deleteJourney() {
        val editor = preferences.edit()
        editor.remove(JOURNEY_TITLE)
        editor.remove(JOURNEY_CONTENT)
        editor.remove(JOURNEY_ORIGIN)
        editor.remove(JOURNEY_DESTINATION)
        editor.remove(JOURNEY_LEVEL)
        editor.remove(JOURNEY_LABELS)
        editor.apply()
        _currentJourney.postValue(null)
    }
}