package com.treflor.internal.eventexcecutor

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class LiveMessageEvent<T> : SingleLiveEvent<(T.() -> Unit)>() {

    fun setEventReceiver(owner: LifecycleOwner, receiver: T) {
        observe(owner, Observer {
            if (it != null) {
                receiver.it()
            }
        })
    }

    fun sendEvent(event: (T.() -> Unit)?) {
        super.setValue(event!!)
    }
}