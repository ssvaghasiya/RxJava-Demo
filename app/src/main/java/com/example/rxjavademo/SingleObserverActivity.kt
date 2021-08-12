package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.core.SingleOnSubscribe
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Single Observable emitting single Note
 * Single Observable is more useful in making network calls
 * where you expect a single response object to be emitted
 * -
 * Single : SingleObserver
 */
class SingleObserverActivity : AppCompatActivity() {

    private val TAG: String = this.javaClass.simpleName
    private var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_observer)

        var noteObservable: Single<Note> = getNoteObservable()

        var singleObserver: SingleObserver<Note> = getSingleObserver()

        noteObservable
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(singleObserver)
    }

    private fun getSingleObserver() : SingleObserver<Note>{
        return object : SingleObserver<Note>{
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe")
                disposable = d
            }

            override fun onSuccess(t: Note?) {
                Log.e(TAG, "onSuccess: " + t?.note);
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError: " + e?.message);
            }

        }
    }

    private fun getNoteObservable() : Single<Note>{
        return Single.create(object : SingleOnSubscribe<Note>{
            override fun subscribe(emitter: SingleEmitter<Note>?) {
                val note = Note(1, "Buy milk!")
                emitter?.onSuccess(note)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable!!.dispose()
    }
}