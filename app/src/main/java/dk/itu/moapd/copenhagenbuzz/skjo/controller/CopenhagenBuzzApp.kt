package dk.itu.moapd.copenhagenbuzz.skjo.controller

import android.app.Application
import com.google.android.material.color.DynamicColors

class CopenhagenBuzzApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic colors to activities if available.
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}