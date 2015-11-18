package com.watchtower.storage.provider

import bolts.CancellationToken
import bolts.CancellationTokenSource
import bolts.Task
import com.pawegio.kandroid.runDelayed
import com.watchtower.networking.ApiClient
import com.watchtower.storage.model.JokeApiModel
import com.watchtower.storage.model.JokeRealmModel
import com.watchtower.storage.model.objectForValue
import com.watchtower.util.get
import com.watchtower.util.realmTransaction


class JokeStorageProvider() {

    //region Monitoring/Polling

    // The caller is responsible of cancelling the polling before starting a new one. This is why the cancellation token is not null
    fun startMonitoring(cancellationToken: CancellationToken, newJokeCallback: (String)->(Any?)) {

        // Return a joke if there is one right away
        JokeStorageProvider().getRandomJoke().onSuccess { task ->
            newJokeCallback.invoke(task.result)
        }

        pollForJoke(cancellationToken, newJokeCallback)
    }

    private fun pollForJoke(cancellationToken: CancellationToken, newJokeCallback: (String)->(Any?) ) {

        // If cancellation was requested then break this recursion
        if( cancellationToken.isCancellationRequested ) {
            return
        }

        // Propagate cancellation to the API
        val cts = CancellationTokenSource()
        cancellationToken.register {
            cts.cancel()
        }

        // Get a joke, if success save it and then return it, after that poll again
        ApiClient().getRandomJoke(cts.token).onSuccess { task ->
            JokeStorageProvider().saveRandomJoke(task.result)

            JokeStorageProvider().getRandomJoke().onSuccess { task ->
                newJokeCallback.invoke(task.result)
            }
        }.continueWith {
            // Keep polling
            // Run delayed every 15 seconds
            runDelayed(15*1000) {
                pollForJoke(cancellationToken, newJokeCallback)
            }
        }
    }

    //endregion

    //region Get/Set Joke to Db

    fun getRandomJoke() : Task<String> {

        var task : Task<String>? = null
        realmTransaction { realm ->
            var realmObject = realm.get<JokeRealmModel>(primaryKey = JokeRealmModel.PRIMARY_KEY, value = JokeRealmModel.DEFAULT_PRIMARY_KEY_VALUE)
            if( realmObject == null) {
                task = Task.forError(Exception("No Joke in the database"))
            } else {
                task = Task.forResult(realmObject.message)
            }
        }

        return task!!
    }

    fun saveRandomJoke(joke: JokeApiModel) {
        realmTransaction { realm ->
            var realmObject = objectForValue(realm, JokeRealmModel.DEFAULT_PRIMARY_KEY_VALUE)
            realmObject.message = joke.value.joke
        }
    }

    //endregion
}