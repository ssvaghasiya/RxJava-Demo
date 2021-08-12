package com.example.rxjavademo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavademo.databinding.ActivityTempBinding


class TempActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityTempBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTempBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }

    private fun initView() {
        binding.buttonViewObservable.setOnClickListener(this)
        binding.buttonViewObservableCustom.setOnClickListener(this)
        binding.buttonViewSingleObservable.setOnClickListener(this)
        binding.buttonViewMaybeObservable.setOnClickListener(this)
        binding.buttonViewCompletableObservable.setOnClickListener(this)
        binding.buttonViewFlowableObservable.setOnClickListener(this)
        binding.buttonViewOperators.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        val item_id = v?.id
        when (item_id) {
            R.id.buttonViewObservable -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonViewObservableCustom -> {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
            R.id.buttonViewSingleObservable -> {
                val intent = Intent(this, SingleObserverActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonViewMaybeObservable -> {
                val intent = Intent(this, MaybeObserverActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonViewCompletableObservable -> {
                val intent = Intent(this, CompletableObserverActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonViewFlowableObservable -> {
                val intent = Intent(this, FlowableObserverActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonViewMerge -> {
                val intent = Intent(this, MergeActivity::class.java)
                startActivity(intent)
            }
            R.id.buttonViewOperators -> {
                val intent = Intent(this, OperatorsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}