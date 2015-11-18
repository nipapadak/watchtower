package com.watchtower.storage.model

import com.watchtower.util.get
import io.realm.Realm

//Returns a Realm Object, Creates if it does not exist
fun objectForValue(realm: Realm, value:String) : JokeRealmModel {
    var realmObject = realm.get<JokeRealmModel>(primaryKey = JokeRealmModel.PRIMARY_KEY, value = value)
    if( realmObject == null ) {
        realmObject = realm.createObject(JokeRealmModel::class.java)
        realmObject?.key = JokeRealmModel.DEFAULT_PRIMARY_KEY_VALUE
    }
    return realmObject!!
}