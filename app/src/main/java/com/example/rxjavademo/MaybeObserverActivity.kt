package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.MaybeObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Consider an example getting a note from db using ID
 * There is possibility of not finding the note by ID in the db
 * In this situation, MayBe can be used
 * -
 * Maybe : MaybeObserver
 */
class MaybeObserverActivity : AppCompatActivity() {

    private val TAG: String = this.javaClass.simpleName
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maybe_observer)

        val noteObservable: Maybe<Note>? = getNoteObservable()

        val noteObserver: MaybeObserver<Note>? = getNoteObserver()

        noteObservable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(noteObserver)
    }

    private fun getNoteObserver(): MaybeObserver<Note>? {
        return object : MaybeObserver<Note> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onSuccess(note: Note) {
                Log.d(TAG, "onSuccess: " + note.note)
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            override fun onComplete() {
                Log.e(TAG, "onComplete")
            }
        }
    }

    private fun getNoteObservable(): Maybe<Note>? {
        return Maybe.create { emitter ->
            val note = Note(1, "Call brother!")
            if (!emitter.isDisposed) {
                emitter.onSuccess(note)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable!!.dispose()
    }
}