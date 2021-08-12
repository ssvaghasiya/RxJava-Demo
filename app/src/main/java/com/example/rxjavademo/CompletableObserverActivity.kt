package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.util.NotificationLite.disposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Completable won't emit any item, instead it returns
 * Success or failure state
 * Consider an example of making a PUT request to server to update
 * something where you are not expecting any response but the
 * success status
 * -
 * Completable : CompletableObserver
 */
class CompletableObserverActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completable_observer)

        val note = Note(1, "Home work!")

        val completableObservable: Completable? = updateNote(note)

        val completableObserver: CompletableObserver? = completableObserver()

        completableObservable
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(completableObserver)
    }

    /**
     * Assume this making PUT request to server to update the Note
     */
    private fun updateNote(note: Note): Completable? {
        return Completable.create { emitter ->
            if (!emitter.isDisposed) {
                Thread.sleep(3000)
                emitter.onComplete()
            }
        }
    }

    private fun completableObserver(): CompletableObserver? {
        return object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
                disposable = d
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: Note updated successfully!")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}