package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


class OperatorsActivity : AppCompatActivity() {

    private val TAG: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operators)
//        skip()
//        skipLast()
//        take()
//        takeLast()
//        distinct()
//        toListOp()
//        exFirst()
        flatMapIterable()
    }

    fun skip() {
        Observable
            .range(1, 10)
            .skip(4)
            .subscribe(object : Observer<Int?> {
                override fun onSubscribe(d: Disposable?) {
                    Log.d(TAG, "Subscribed")
                }

                override fun onNext(integer: Int?) {
                    Log.d(TAG, "onNext: $integer")
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    Log.d(TAG, "Completed")
                }
            })
    }

    fun skipLast() {
        Observable
            .range(1, 10)
            .skipLast(4)
            .subscribe(object : Observer<Int> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "Subscribed")
                }

                override fun onNext(integer: Int) {
                    Log.d(TAG, "onNext: $integer")
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    Log.d(TAG, "Completed")
                }
            })
    }

    fun take() {
        Observable
            .range(1, 10)
            .take(4)
            .subscribe(object : Observer<Int> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "Subscribed")
                }

                override fun onNext(integer: Int) {
                    Log.d(TAG, "onNext: $integer")
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    Log.d(TAG, "Completed")
                }
            })
    }

    fun takeLast() {
        Observable
            .range(1, 10)
            .takeLast(4)
            .subscribe(object : Observer<Int> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "Subscribed")
                }

                override fun onNext(integer: Int) {
                    Log.d(TAG, "onNext: $integer")
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    Log.d(TAG, "Completed")
                }
            })
    }

    fun distinct() {
        val numbersObservable = Observable.just(10, 10, 15, 20, 100, 200, 100, 300, 20, 100)

        numbersObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .distinct()
            .subscribe(object : Observer<Int> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(integer: Int) {
                    Log.d(TAG, "onNext: $integer")
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun toListOp() {
        Observable.just(2, 1, 3)
            .toList()
            .subscribe { x -> Log.d(TAG, x.toString()) };
    }

    fun exFirst() {
        val numbersObservable = Observable.just(10, 10, 15, 20, 100, 200, 100, 300, 20, 100)

        numbersObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .first(0)
            .subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onSuccess(t: Int?) {
                    Log.d(TAG, "onSuccess: $t")
                }
            })
    }

    private fun flatMapIterable() {
        getIds()
            ?.flatMapIterable { ids: List<Int?>? -> ids } // Converts your list of ids into an Observable which emits every item in the list
            ?.flatMap { item -> getItemObservable(item!!) } // Calls the method which returns a new Observable<Item>
            ?.subscribe { item: Item ->
                Log.e(
                    TAG,
                    "item: " + item.id
                )
            }
    }

    private fun getIds(): Observable<List<Int?>>? {
        return Observable.just(listOf(1, 2, 3))
    }

    private fun getItemObservable(id: Int): Observable<Item>? {
        val item: Item = Item()
        item.id = id
        return Observable.just(item)
    }

    class Item {
        var id = 0
    }

}