package com.watchtower.activity


import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import bolts.CancellationTokenSource
import butterknife.bindView
import com.watchtower.R
import com.watchtower.storage.provider.JokeStorageProvider

//The single Activity of this Application,
class WTActivity : Activity() {

    //region Properties

    private val randomJokeTextView: TextView by bindView(R.id.random_joke_text_view)
    private val provider = JokeStorageProvider()
    private var cts : CancellationTokenSource? = null

    //endregion

    //region Activity Lifecycle Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchtower)
    }

    override fun onResume() {
        super.onResume()
        cts = CancellationTokenSource()
        provider.startMonitoring(cts!!.token) { message ->
            runOnUiThread{ randomJokeTextView.text = message }
            null
        }
    }

    override fun onPause() {
        super.onPause()
        cts?.cancel()
    }

    //endregion
}
