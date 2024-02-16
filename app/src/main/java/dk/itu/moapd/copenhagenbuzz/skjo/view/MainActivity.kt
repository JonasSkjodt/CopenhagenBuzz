package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.copenhagenbuzz.skjo.R
import dk.itu.moapd.copenhagenbuzz.skjo.controller.MainViewModel
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
     * viewModel
     * viewModel sorts rotation data bug with Android Jetpack
     *
     * @see slide3 https://learnit.itu.dk/pluginfile.php/383364/mod_resource/content/0/Slides%20%2303.pdf
     */
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    /**
     * Sets up the current layout, the view bindings, and listeners.
     * When the add event button is clicked, it validates the input fields and then creates this new event (if all fields are non-empty).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //make sure the toolbar is there (header menu)
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        // Initialize isLoggedIn state from intent only if savedInstanceState is null (rotation bug, @see viewModel)
        if (savedInstanceState == null) {
            viewModel.isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        }

        setupUI()
    }

    /**
     * Inflates the menu resource (defined in XML) into the Menu provided in the parameter.
     *
     * @param menu The options menu in which we place your items (currently linked to top_app_bar.xml).
     * @return Boolean Return true to display the menu; if we return false it will not be shown.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }
    /**
     * Prepares the screen's standard options menu to be displayed.
     * It shows or hides the menu items based on whether the user is logged in or not.
     *
     * @param menu The options menu as last shown or first initialized by onCreateOptionsMenu().
     * @return Boolean we must return true for the menu to be displayed; if we return false it will not be shown.
     */
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.user_account_item)?.isVisible = !viewModel.isLoggedIn
        menu.findItem(R.id.guest_account_item)?.isVisible = viewModel.isLoggedIn

        return super.onPrepareOptionsMenu(menu)
    }
    /**
     * This method is called whenever an item in the options menu is selected.
     * The default returns false.
     * (for now it only handles guests being able to click back to the login page)
     *
     * @param item The menu item that was selected.
     * @return Boolean Return false to allow normal menu processing
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.user_account_item -> {
                // Intent to start LoginActivity
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                true
            }
            // Add more cases for other menu items as we go to the next exercises
            else -> super.onOptionsItemSelected(item)
        }
    }
    /**
     * SetupUI() Initializes the UI components (viewBindings references our UI components)
     */
    private fun setupUI() {
        val eventName = binding.contentMain.editTextEventName
        val eventLocation = binding.contentMain.editTextEventLocation
        val eventType = binding.contentMain.editEventType
        val eventDescription = binding.contentMain.editEventDescription
        val addEventButton = binding.contentMain.fabAddEvent
        val eventDate = binding.contentMain.fieldEventDate

        // Set up event listeners
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

                // Create a new event instance from the event data class
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