package com.example.rxjavademo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Flowable observable should be used when an Observable is generating huge amount of events/data than the Observer can handle. As per doc, Flowable can be used when the source is generating 10k+ events and subscriber can’t consume it all.
 */

/**
 * Simple example of Flowable just to show the syntax
 * the use of Flowable is best explained when used with BackPressure
 * Read the below link to know the best use cases to use Flowable operator
 * https://github.com/ReactiveX/RxJava/wiki/What%27s-different-in-2.0#when-to-use-flowable
 * -
 * Flowable : SingleObserver
 */

/**
 * Backpressure
 * When Flowable is used, the overflown emissions has to be handled using a strategy called Backpressure. Otherwise it throws an exception such as MissingBackpressureException or OutOfMemoryError. Backpressure is very important topic and needs a separate article to understand it clearly.
 */

class FlowableObserverActivity : AppCompatActivity() {

    private val TAG: String = this.javaClass.simpleName
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flowable_observer)

        val flowableObservable: Flowable<Int>? = getFlowableObservable()

        val observer: SingleObserver<Int?>? = getFlowableObserver()

        flowableObservable
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.reduce(0, object : BiFunction<Int?, Int?, Int?> {
                override fun apply(result: Int?, number: Int?): Int? {
                    Log.e(TAG, "Result: " + result + ", new number: " + number);
                    return result!! + number!!
                }
            })
            ?.subscribe(observer)
    }

    private fun getFlowableObserver(): SingleObserver<Int?>? {
        return object : SingleObserver<Int?> {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
                disposable = d
            }

            override fun onSuccess(@NonNull integer: @NonNull Int?) {
                Log.d(TAG, "onSuccess: $integer")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }
        }
    }

    private fun getFlowableObservable(): Flowable<Int>? {
        return Flowable.range(1, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable!!.dispose()
    }
}