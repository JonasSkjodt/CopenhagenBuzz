package dk.itu.moapd.copenhagenbuzz.skjo

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        setContentView(binding.root)

        //find the ids and bind them to the variables
        eventName = findViewById(R.id.edit_text_event_name)
        eventLocation = findViewById(R.id.edit_text_event_location)
        eventType = findViewById(R.id.edit_event_type)
        eventDescription = findViewById(R.id.edit_event_description)
        addEventButton = findViewById(R.id.fab_add_event)
        eventDate = findViewById(R.id.field_event_date)

        eventDate.editText?.setOnClickListener {
            showDatePicker()
        }

        // Set listeners
        eventDate.setEndIconOnClickListener {
            showDatePicker()
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
                // https://stackoverflow.com/questions/48253107/what-does-do-in-kotlin-elvis-operator
                event.setEventDate(eventDate.editText?.text.toString().trim() ?: "")
                event.setEventType(eventType.text.toString().trim())
                event.setEventDescription(eventDescription.text.toString().trim())

                showMessage()
            }
        }
    }

    //show the material date picker
    // https://github.com/material-components/material-components-android/blob/master/docs/components/DatePicker.md
    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {
            val date = Date(it)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            eventDate.editText?.setText(formattedDate)
        }
    }
    private fun showMessage() {
        Log.d(TAG, event.toString())
        Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show()
    }
}