package com.example

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Accessibility Event received for diagnostics/monitoring
        if (event != null) {
            Log.d("BatteryHeroAccessibility", "Received event: ${event.eventType}")
        }
    }

    override fun onInterrupt() {
        Log.d("BatteryHeroAccessibility", "Accessibility Service interrupted")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("BatteryHeroAccessibility", "Accessibility Service connected successfully")
    }
}
