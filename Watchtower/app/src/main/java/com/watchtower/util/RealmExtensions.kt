package com.watchtower.util

import io.realm.Realm
import io.realm.RealmObject

// Helper Realm Transaction Method
fun realmTransaction(block: (realm: Realm) -> Unit) {
    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    try {
        block(realm)
    } catch(exception: Exception) {
        exception.printStackTrace()
        throw exception
    } finally {
        realm.commitTransaction()
        realm.close()
    }
}

//Helper get object of type from Realm
inline fun <reified T : RealmObject> Realm.get(primaryKey: String, value: String): T? {
    return where(T::class.java).equalTo(primaryKey, value).findFirst()
}