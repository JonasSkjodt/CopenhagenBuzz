package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.app.Application
import com.google.android.material.color.DynamicColors

/**
 * This class is the starting point of the application and is used for initial setup.
 * It inherits from the Android [Application] class and overrides the [onCreate] method
 * to apply dynamic color theming to all activities if available
 */
class CopenhagenBuzzApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic colors to activities if available
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}