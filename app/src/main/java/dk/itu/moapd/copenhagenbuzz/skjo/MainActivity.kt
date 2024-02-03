package dk.itu.moapd.copenhagenbuzz.skjo

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.util.Pair

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // A set of private constants used in this class.
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    //GUI variables
    private lateinit var eventName: EditText
    private lateinit var eventLocation: EditText
    private lateinit var eventDate: TextInputLayout
    private lateinit var eventType: EditText
    private lateinit var eventDescription: EditText
    private lateinit var addEventButton: FloatingActionButton

    // An instance of the 'Event' class
    private val event: Event = Event("","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        //splash screen start
        Thread.sleep(2000)
        installSplashScreen()
        //splash screen end

        setContentView(binding.root)

        //find the ids and bind them to the variables
        eventName = findViewById(R.id.edit_text_event_name)
        eventLocation = findViewById(R.id.edit_text_event_location)
        eventType = findViewById(R.id.edit_event_type)
        eventDescription = findViewById(R.id.edit_event_description)
        addEventButton = findViewById(R.id.fab_add_event)
        eventDate = findViewById(R.id.field_event_date)

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

                event.setEventName(eventName.text.toString().trim())
                event.setEventLocation(eventLocation.text.toString().trim())
                //?: is the elvis operator to handle a potential nullpointerexception in Kotlin
                //https://stackoverflow.com/questions/48253107/what-does-do-in-kotlin-elvis-operator
                event.setEventDate(eventDate.editText?.text.toString().trim() ?: "")
                event.setEventType(eventType.text.toString().trim())
                event.setEventDescription(eventDescription.text.toString().trim())

                showMessage()
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

                eventDate.editText?.setText(dateRangeText)
        }
    }
    private fun showMessage() {
        Log.d(TAG, event.toString())
        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show()
    }
}