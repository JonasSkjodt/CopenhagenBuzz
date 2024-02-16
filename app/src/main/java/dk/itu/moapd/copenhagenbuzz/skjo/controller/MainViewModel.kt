package dk.itu.moapd.copenhagenbuzz.skjo.controller

import androidx.lifecycle.ViewModel

/**
 * ViewModel keeps an activity’s user interface state data in
 * memory across configuration changes.
 */
class MainViewModel : ViewModel() {
    var isLoggedIn: Boolean = false
}