package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.service.AutoScrollAccessibilityService
import com.example.service.AutoScrollController
import com.example.ui.MainAppScaffold
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScaffold()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val isEnabled = AutoScrollAccessibilityService.isServiceRunning() ||
            AutoScrollAccessibilityService.isAccessibilitySettingsEnabled(this)
        AutoScrollController.setServiceConnected(isEnabled)
    }
}
