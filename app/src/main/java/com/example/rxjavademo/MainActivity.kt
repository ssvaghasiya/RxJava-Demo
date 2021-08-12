package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class MainActivity : AppCompatActivity() {

    var TAG: String = this.javaClass.simpleName
    private var disposable: Disposable? = null
    private val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animalsObservable: Observable<String>? = getAnimalsObservable()
        val animalsObserver0: Observer<String>? = getAnimalsObserver0()
//
        animalsObservable
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnError { error ->
                Log.e(TAG, "Error: " + error.message)
            }
            ?.filter(object : Predicate<String?> {
                @Throws(Exception::class)
                override fun test(t: String?): Boolean {
                    return t!!.lowercase().startsWith("c")
                }
            })
            ?.subscribeWith(animalsObserver0)

//
//
//        val animalsObserver: DisposableObserver<String>? = getAnimalsObserver()
//        val animalsObserverAllCaps: DisposableObserver<String>? = getAnimalsAllCapsObserver()
//
//        compositeDisposable.add(
//            animalsObservable
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.filter { t -> t!!.lowercase().startsWith("b") }
//                ?.subscribeWith(animalsObserver)
//        )
//
//        compositeDisposable.add(
//            animalsObservable
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.filter { s -> s.lowercase().startsWith("c") }
//                ?.map { s -> s.uppercase() }
//                ?.subscribeWith(animalsObserverAllCaps)
//        )

//        onErrorReturnItem()
//        onErrorReturn()
//        onRetry()

//        doOnComplete()
//        exampleJustArray()
//        exampleRange()
    }

    fun exampleRange() {
        Observable.range(1, 10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Int> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(integer: Int) {
                    Log.d(TAG, "onNext: $integer")
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun exampleJustArray() {
        val numbers = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        Observable.just(numbers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Array<Int>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(integers: Array<Int>) {
                    Log.d(TAG, "onNext: " + integers.size)
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun doOnComplete() {
        Observable.fromArray(1, 2, 3)
            .doOnComplete {
                //We will do this when it is completed
                Log.e("Result", "Completed")
            }
            .doFinally {
                Log.e("Result", "Finally")

            }
            .subscribe(
                { x: Any? -> Log.e(TAG, x.toString()) },
                { error: Throwable ->
                    Log.e(TAG, error.message.toString())
                }
            ) { Log.e(TAG, "Comp...") }
    }

    fun onErrorReturnItem() {
        Observable.fromArray(1, 2, 3)
            .doOnNext {
                if (it == 2) {
                    throw (RuntimeException("Exception on 2"))
                }
            }
            .onErrorReturnItem(-1)
            .subscribe(
                { x: Any? -> Log.e(TAG, x.toString()) },
                { error: Throwable ->
                    Log.e(TAG, error.message.toString())
                }
            ) { Log.e(TAG, "Complete") }
    }

    fun onErrorReturn() {
        Observable.fromArray(1, 2, 3)
            .doOnNext {
                if (it == 2) {
                    throw (RuntimeException("Exception on 2"))
                }
            }
            .onErrorReturn { t: Throwable ->
                if (t.message == "Exception on 2") {
                    1 // Return a value based on error type
                } else {
                    100 // Return another value based on different error type
                }
            }
            .subscribe(
                { x: Any? -> Log.e(TAG, x.toString()) },
                { error: Throwable ->
                    Log.e(TAG, error.message.toString())
                }
            ) { Log.e(TAG, "Complete") }
    }

    fun onRetry() {
        Observable.fromArray(1, 2, 3)
            .doOnNext {
                if (it == 2) {
                    throw (RuntimeException("Exception on 2"))
                }
            }
            .retry(3)
            .subscribe(
                { x: Any? -> Log.e(TAG, x.toString()) },
                { error: Throwable ->
                    Log.e(TAG, error.message.toString())
                }
            ) { Log.e(TAG, "Complete") }
    }

    private fun getAnimalsObserver0(): Observer<String>? {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.e(TAG, "onSubscribe")
                disposable = d;
            }

            override fun onNext(s: String) {
                Log.e(TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            override fun onComplete() {
                Log.e(TAG, "All items are emitted!")
            }
        }
    }


    private fun getAnimalsObserver(): DisposableObserver<String>? {
        return object : DisposableObserver<String>() {
            override fun onNext(s: String) {
                Log.d(TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            override fun onComplete() {
                Log.d(TAG, "All items are emitted!")
            }
        }
    }

    private fun getAnimalsAllCapsObserver(): DisposableObserver<String>? {
        return object : DisposableObserver<String>() {
            override fun onNext(s: String) {
                Log.d(TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            override fun onComplete() {
                Log.d(TAG, "All items are emitted!")
            }
        }
    }

    private fun getAnimalsObservable_(): Observable<String>? {
        return Observable.just("Ant", "Bee", "Cat", "Dog", "Fox")
    }

    private fun getAnimalsObservable(): Observable<String>? {
        return Observable.fromArray(
            "Ant", "Ape",
            "Bat", "Bee", "Bear", "Butterfly",
            "Cat", "Crab", "Cod",
            "Dog", "Dove",
            "Fox", null
        )
    }


    override fun onDestroy() {
        super.onDestroy()

        // don't send events once the activity is destroyed
//        disposable!!.dispose()
        compositeDisposable.clear();
    }

}