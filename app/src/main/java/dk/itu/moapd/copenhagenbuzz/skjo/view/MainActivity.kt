package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.util.Pair
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

/**
 * KDoc the code
 * https://kotlinlang.org/docs/kotlin-doc.html#sample-identifier
 * https://source.android.com/docs/core/architecture/hidl/code-style
 */

/**
 * The MainActivity class handles user interactions (like events) and initializes the UI in the app
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // A set of private constants used in this class.
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    // An instance of the 'Event' class
    private val event: Event = Event("","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        //splash screen start
        Thread.sleep(1500)
        installSplashScreen()
        //splash screen end

        setContentView(binding.root)

        //the viewBindings references our UI components
        //kotlin changes the id from, for instance, edit_text_event_name to editTextEventName.
        //https://developer.android.com/topic/libraries/view-binding
        val eventName = binding.contentMain.editTextEventName
        val eventLocation = binding.contentMain.editTextEventLocation
        val eventType = binding.contentMain.editEventType
        val eventDescription = binding.contentMain.editEventDescription
        val addEventButton = binding.contentMain.fabAddEvent
        val eventDate = binding.contentMain.fieldEventDate

        eventDate.editText?.setOnClickListener {
            showDateRangePicker()
        }

        // Set listeners
        eventDate.setEndIconOnClickListener {
            showDateRangePicker()
        }

        addEventButton.setOnClickListener {
            if (eventName.text.toString().isNotEmpty() &&
                eventLocation.text.toString().isNotEmpty() &&
                eventDate.editText?.text.toString().isNotEmpty() &&
                eventType.text.toString().isNotEmpty() &&
                eventDescription.text.toString().isNotEmpty()) {

                //Create a new event instances from the event data class
                val event = Event(
                    eventName = eventName.text.toString().trim(),
                    eventLocation = eventLocation.text.toString().trim(),
                    eventDate = eventDate.editText?.text.toString().trim() ?: "",
                    eventType = eventType.text.toString().trim(),
                    eventDescription = eventDescription.text.toString().trim()
                )
                showMessage(event)
            }
        }
    }
    //show the material date picker
    //https://github.com/material-components/material-components-android/blob/master/docs/components/DatePicker.md
    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(
                //pair is a container to ease passing around a tuple of two objects.
                //https://developer.android.com/reference/kotlin/androidx/core/util/package-summary
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

        dateRangePicker.show(supportFragmentManager, "DATE_RANGE_PICKER")

        // stitched together from
        // https://www.geeksforgeeks.org/how-to-implement-date-range-picker-in-android/
        dateRangePicker.addOnPositiveButtonClickListener {
            selection:
                Pair<Long, Long> ->
                val startDate = Date(selection.first)
                val endDate = Date(selection.second)
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                //format the picked dates
                val formattedStartDate = formatter.format(startDate)
                val formattedEndDate = formatter.format(endDate)
                //now give us the two dates in the right format
                val dateRangeText = "$formattedStartDate - $formattedEndDate"

                val eventDate = binding.contentMain.fieldEventDate
                eventDate.editText?.setText(dateRangeText)
        }
    }
    private fun showMessage(event: Event) {
        Log.d(TAG, event.toString())
        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show()
    }
}