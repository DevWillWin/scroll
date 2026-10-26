package com.example

import android.app.Application
import com.example.service.AutoScrollController

class ReelScrollApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AutoScrollController.initialize(this)
    }
}
