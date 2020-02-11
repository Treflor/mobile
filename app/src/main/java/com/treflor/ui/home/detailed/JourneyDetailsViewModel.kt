package com.treflor.ui.home.detailed

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.eventexcecutor.LiveMessageEvent
import com.treflor.internal.lazyDeferred
import kotlinx.coroutines.*

class JourneyDetailsViewModel(
    private val repository: Repository,
    private val journeyId: String
) : ViewModel() {

    val journey by lazyDeferred {
        repository.getJourneyById(journeyId)
    }
    val userId by lazy { repository.getCurrentUserId() }

    val liveMessageEvent =
        LiveMessageEvent<ActivityNavigation>()

    fun addToFavorite() = runBlocking {
        val response = withContext(Dispatchers.IO) {
            return@withContext repository.addJourneyFavorite(journeyId)
        }
        if(response.success) GlobalScope.launch(Dispatchers.Main) { liveMessageEvent.sendEvent { showSnackBar("Added to favorite!") } }
    }

    fun removeFromFavorite() = runBlocking {
        val response = withContext(Dispatchers.IO) {
            return@withContext repository.removeJourneyFavorite(journeyId)
        }
        if(response.success) GlobalScope.launch(Dispatchers.Main) { liveMessageEvent.sendEvent { showSnackBar("remove from favorite!") } }
    }
}
