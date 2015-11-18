package com.watchtower.networking

import android.content.Context
import bolts.CancellationToken
import bolts.Task
import bolts.TaskCompletionSource
import com.watchtower.storage.model.JokeApiModel
import retrofit.*


class ApiClient() {

    //region Properties

    private val service: APIEndpointInterface

    //endregion

    //region Init

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.icndb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIEndpointInterface::class.java);
    }

    //endregion

    //region Public API Methods

    fun getRandomJoke(cancellationToken: CancellationToken) : Task<JokeApiModel> {

        val tcs = TaskCompletionSource<JokeApiModel>()

        val call = service.getRandomJoke()

        // Cancel web call as soon as we request cancellation
        cancellationToken.register {
            call.cancel()
        }

        // Enqueue this call so it gets executed asyncronously on a bg thread
        call.enqueue( object: Callback<JokeApiModel> {

            override fun onFailure(t: Throwable?) {
                tcs.setError(Exception(t?.message))
            }

            override fun onResponse(response: Response<JokeApiModel>?, retrofit: Retrofit?) {
                if( response != null ) {
                    tcs.setResult(response.body())
                } else {
                    tcs.setError(Exception("Null Response"))
                }
            }
        })

        return tcs.task
    }

    //endregion
}

