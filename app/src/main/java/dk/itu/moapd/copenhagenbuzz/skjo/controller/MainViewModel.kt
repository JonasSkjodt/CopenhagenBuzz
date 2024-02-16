package dk.itu.moapd.copenhagenbuzz.skjo.controller

import androidx.lifecycle.ViewModel

/**
 * MainViewModel
 * MainViewModel extends ViewModel which is an android jetpack feature.
 * It keeps an activity’s user interface state data in memory across configuration changes.
 * This means that data will still be stored when the phone is rotated.
 */
class MainViewModel : ViewModel() {
    var isLoggedIn: Boolean = false
}