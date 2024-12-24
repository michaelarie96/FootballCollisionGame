package com.example.hw1_208388124

import android.app.Application
import com.example.hw1_208388124.utillities.SignalManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
    }
}