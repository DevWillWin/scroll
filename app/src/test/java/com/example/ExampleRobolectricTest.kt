package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PreferencesManager
import com.example.model.ScrollPreset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ReelScroll", appName)
    }

    @Test
    fun `preferences manager presets work correctly`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = PreferencesManager(context)
        prefs.selectedPreset = ScrollPreset.ONE_MINUTE
        assertEquals(60, prefs.getEffectiveDurationSeconds())

        prefs.selectedPreset = ScrollPreset.TWO_MINUTE
        assertEquals(120, prefs.getEffectiveDurationSeconds())
    }
}
