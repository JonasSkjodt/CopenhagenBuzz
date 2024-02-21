package dk.itu.moapd.copenhagenbuzz.skjo.controller

import android.app.Application
import com.google.android.material.color.DynamicColors

/**
 * this class makes sure the app takes the dynamic colors of the user's system
 */
class CopenhagenBuzzApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic colors to activities if available.
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}