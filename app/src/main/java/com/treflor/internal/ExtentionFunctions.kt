package com.treflor.internal

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObservers(){
    this.value = this.value
}