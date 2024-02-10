package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.util.Pair
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

/**
 * The MainActivity class handles user interactions (like events) and initializes the UI in the app
 *
 * KDoc the code
 * @see https://kotlinlang.org/docs/kotlin-doc.html#sample-identifier
 * @see https://source.android.com/docs/core/architecture/hidl/code-style
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // A set of private constants used in this class.
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    //An instance of the 'Event' class
    private val event: Event = Event("","","","","")

    /**
     * Sets up the current layout, the view bindings, and listeners.
     * When the add event button is clicked, it validates the input fields and then creates this new event (if all fields are non-empty).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        //event listeners for the eventDate calendar
        with(eventDate) {
            editText?.setOnClickListener {
                showDateRangePicker()
            }

            setEndIconOnClickListener {
                showDateRangePicker()
            }
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

    /**
     * The dateRangePicker method lets the user choose the dates for the event.
     * The selection is set to the current month's beginning and today's date in UTC milliseconds.
     *
     * @see [MaterialDatePicker](https://github.com/material-components/material-components-android/blob/master/docs/components/DatePicker.md)
     */
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

            //shows the two dates in the right format
            val dateRangeText = "$formattedStartDate - $formattedEndDate"

            val eventDate = binding.contentMain.fieldEventDate
            eventDate.editText?.setText(dateRangeText)
        }
    }

    /**
     * After the event has been added, the showMessage functions shows the message
     * with the event info. It uses Android's Snackbar component to present the message
     * to the user.
     *
     * @param event The Event object containing information about the event that has been added.
     *
     * @see [Snackbar](https://developer.android.com/reference/com/google/android/material/snackbar/Snackbar)
     *
     */
    private fun showMessage(event: Event) {
        // Convert the event details to a string message
        val message = "Event created: \nName: ${event.eventName} " +
                "Location: ${event.eventLocation} " +
                "Date: ${event.eventDate} " +
                "Type: ${event.eventType} " +
                "Description: ${event.eventDescription}"

        // Show Snackbar with the message
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE).apply {
            show()
        }
    }
}