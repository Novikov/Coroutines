package com.example.coroutinescancelation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    lateinit var job: Job

    private val scope = CoroutineScope(Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.run).setOnClickListener {
            onRun4()
        }

        findViewById<View>(R.id.cancel).setOnClickListener {
            onCancel()
        }
    }

    private fun onRun() {
        log("onRun, start")

        scope.launch {
            log("coroutine, start")
            TimeUnit.MILLISECONDS.sleep(1000)
            log("coroutine, end")
        }

        log("onRun, middle")

        scope.launch {
            log("coroutine2, start")
            TimeUnit.MILLISECONDS.sleep(1500)
            log("coroutine2, end")
        }

        log("onRun, end")
    }

    private fun onRun2() {
        log("onRun, start")

        job = scope.launch {
            log("coroutine, start")
            var x = 0
            while (x < 5) {
                TimeUnit.MILLISECONDS.sleep(1000)
                log("coroutine, ${x++}")
            }
            log("coroutine, end")
        }

        log("onRun, end")
    }

    private fun onRun3() {
        log("onRun, start")

        job = scope.launch {
            log("coroutine, start")
            var x = 0
            while (x < 5 && isActive) {
                TimeUnit.MILLISECONDS.sleep(1000)
                log("coroutine, ${x++}, isActive = $isActive")
            }
            log("coroutine, end")
        }

        log("onRun, end")
    }

    private fun onRun4() {
        log("onRun, start")

        job = scope.launch {
            log("coroutine, start")
            var x = 0
            while (x < 5 && isActive) {
                delay(1000)
                log("coroutine, ${x++}, isActive = $isActive")
            }
            log("coroutine, end")
        }

        log("onRun, end")
    }

    private fun onCancel() {
        log("onCancel")
        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }

    private fun log(text: String) {
        Log.d("TAG", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }
}
