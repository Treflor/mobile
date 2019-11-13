package com.treflor.data.provider

interface JWTProvider {
    fun getJWT():String
    fun setJWT(token:String):Boolean
    fun unsetJWT():Boolean
}