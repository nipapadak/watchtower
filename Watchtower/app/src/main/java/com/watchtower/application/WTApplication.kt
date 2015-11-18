package com.watchtower.application

import android.app.Application
import android.os.StrictMode
import bolts.Task
import io.realm.Realm
import io.realm.RealmConfiguration

class WTApplication : Application() {

    init {
        StrictMode.enableDefaults()
    }

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    // Realm Init & Migration in the background
    fun initRealm() {
        Task.callInBackground {
            val realmVersion: Long = 0
            val realmConfiguration = RealmConfiguration.Builder(this)
                    .schemaVersion(realmVersion)
                    .migration { realm: Realm, version: Long -> realmVersion }
                    .build()

            Realm.setDefaultConfiguration(realmConfiguration)
        }
    }
}