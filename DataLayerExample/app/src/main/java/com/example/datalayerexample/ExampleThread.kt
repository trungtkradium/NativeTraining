package com.example.datalayerexample

import android.os.Handler
import android.os.Looper

class ExampleThread : Thread() {
    lateinit var handler: Handler
    private lateinit var looper: Looper

    override fun run() {
        Looper.prepare()
        looper = Looper.myLooper()!!
        handler = Handler(looper)
        if (this.isInterrupted) return
        Looper.loop()
        super.run()
    }
}