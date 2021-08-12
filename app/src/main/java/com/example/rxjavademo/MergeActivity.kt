package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MergeActivity : AppCompatActivity() {

    private val TAG: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge)

        mergeCustomObservables()

//        exMerge()
//        exMergeArray()
//        exMergeIterable()
//        exMergeWith()
//        exMergeInfinite()
    }

    private fun exMerge() {
        val oneToFive = Observable.just(1, 2, 3, 4, 5)
        val sixToTen = Observable.just(6, 7, 8, 9, 10)
        Observable.merge(sixToTen, oneToFive).subscribe { x: Int? ->
            Log.e(TAG, x.toString())
        }
    }

    private fun exMergeArray() {
        val oneToFive = Observable.just(1, 2, 3, 4, 5)
        val sixToTen = Observable.just(6, 7, 8, 9, 10)
        val elevenToFifteen = Observable.just(11, 12, 13, 14, 15)
        val sixteenToTwenty = Observable.just(16, 17, 18, 19, 20)
        val twentyOneToTwentyFive = Observable.just(21, 22, 23, 24, 25)
        Observable.mergeArray(
            oneToFive,
            sixToTen,
            elevenToFifteen,
            sixteenToTwenty,
            twentyOneToTwentyFive
        )
            .subscribe { x: Int? -> Log.e(TAG, x.toString()) }
    }

    private fun exMergeIterable() {
        val oneToFive = Observable.just(1, 2, 3, 4, 5)
        val sixToTen = Observable.just(6, 7, 8, 9, 10)
        val elevenToFifteen = Observable.just(11, 12, 13, 14, 15)
        val sixteenToTwenty = Observable.just(16, 17, 18, 19, 20)
        val twentyOneToTwentyFive = Observable.just(21, 22, 23, 24, 25)
        val observableList: List<Observable<Int>> = Arrays.asList(
            oneToFive,
            sixToTen,
            elevenToFifteen,
            sixteenToTwenty,
            twentyOneToTwentyFive
        )
        Observable.merge(observableList).subscribe { x: Int? ->
            Log.e(TAG, x.toString())
        }
    }

    private fun exMergeWith() {
        val oneToFive = Observable.just(1, 2, 3, 4, 5)
        val sixToTen = Observable.just(6, 7, 8, 9, 10)
        oneToFive.mergeWith(sixToTen).subscribe { x: Int? ->
            Log.e(TAG, x.toString())
        }
    }

    private fun exMergeInfinite() {
        val infinite1 = Observable.interval(1, TimeUnit.SECONDS)
            .map { item: Long -> "From infinite1: $item" }
        val infinite2 = Observable.interval(2, TimeUnit.SECONDS)
            .map { item: Long -> "From infinite2: $item" }
        infinite1.mergeWith(infinite2).subscribe { x: String? ->
            Log.e(TAG, x.toString())
        }
//        Thread.sleep(6050)
    }

    fun mergeCustomObservables() {
        Observable
            .merge(getMaleObservable(), getFemaleObservable())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<User?> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(user: User?) {
                    Log.e(TAG, user?.name + ", " + user?.gender)
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {}

            })
    }

    private fun getFemaleObservable(): Observable<User?>? {
        val names = arrayOf("Lucy", "Scarlett", "April")
        val users: MutableList<User> = ArrayList()
        for (name in names) {
            val user = User()
            user.name = (name)
            user.gender = ("female")
            users.add(user)
        }
        return Observable
            .create(ObservableOnSubscribe<User?> { emitter ->
                for (user in users) {
                    if (!emitter.isDisposed) {
//                        Thread.sleep(1000)
                        emitter.onNext(user)
                    }
                }
                if (!emitter.isDisposed) {
                    emitter.onComplete()
                }
            }).subscribeOn(Schedulers.io())
    }

    private fun getMaleObservable(): Observable<User?>? {
        val names = arrayOf("Mark", "John", "Trump", "Obama")
        val users: MutableList<User> = ArrayList()
        for (name in names) {
            val user = User()
            user.name = (name)
            user.gender = ("male")
            users.add(user)
        }
        return Observable
            .create(ObservableOnSubscribe<User?> { emitter ->
                for (user in users) {
                    if (!emitter.isDisposed) {
//                        Thread.sleep(500)
                        emitter.onNext(user)
                    }
                }
                if (!emitter.isDisposed) {
                    emitter.onComplete()
                }
            }).subscribeOn(Schedulers.io())
    }

    inner class User() {
        var name: String? = null
        var gender: String? = null
    }
}