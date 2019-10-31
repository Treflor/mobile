package com.treflor

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class TreflorApplication:Application(),KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        //TODO bind things
    }

}