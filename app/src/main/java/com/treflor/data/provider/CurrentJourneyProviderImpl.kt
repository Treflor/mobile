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
const val JOURNEY_IMAGE = "journey_image"
const val JOURNEY_IMAGES = "journey_images_"
const val JOURNEY_IMAGES_ID = "journey_images_id"

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
        val image = preferences.getString(JOURNEY_IMAGE, " ")
        return if (!title.isNullOrEmpty()) Journey(
            "",
            title,
            content!!,
            origin,
            destination,
            level!!,
            labels!!.toList(),
            image!!
        ) else null
    }

    override fun persistImages(base64Images: List<String>) {
        base64Images.forEach {
            preferences.edit().putString(JOURNEY_IMAGES + getImageId(), it).apply()
            increaseImageId()
        }
    }

    override fun getImages(): List<String> {
        val images = mutableListOf<String>()
        for (i in 0 until getImageId()) {
            images.add(preferences.getString(JOURNEY_IMAGES + i, "")!!)
        }
        return images
    }

    override fun deleteImages() {
        for (i in 0 until getImageId()) {
            preferences.edit().remove(JOURNEY_IMAGES + i).apply()
        }
        preferences.edit().remove(JOURNEY_IMAGES_ID).apply()
    }

    override fun persistCurrentJourney(journey: Journey) {
        val editor = preferences.edit()
        editor.putString(JOURNEY_TITLE, journey.title)
        editor.putString(JOURNEY_CONTENT, journey.content)
        editor.putString(JOURNEY_ORIGIN, Gson().toJson(journey.origin))
        editor.putString(JOURNEY_DESTINATION, Gson().toJson(journey.destination))
        editor.putString(JOURNEY_LEVEL, journey.level)
        editor.putStringSet(JOURNEY_LABELS, journey.labels.toSet())
        editor.putString(JOURNEY_IMAGE, journey.image)
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
        editor.remove(JOURNEY_IMAGE)
        editor.apply()
        _currentJourney.postValue(null)
    }

    private fun getImageId(): Int = preferences.getInt(JOURNEY_IMAGES_ID, 0)
    private fun increaseImageId() =
        preferences.edit().putInt(JOURNEY_IMAGES_ID, getImageId() + 1).apply()
}