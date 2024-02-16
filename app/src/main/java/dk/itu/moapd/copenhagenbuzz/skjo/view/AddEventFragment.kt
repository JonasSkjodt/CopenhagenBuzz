package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentAddEventBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEventFragment : Fragment(){

    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!

    /**
     * onCreateView
     * @see [onCreateView](https://github.com/material-components/material-components-android/blob/master/docs/components/BottomSheet.md)
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    /**
     * SetupUI() Initializes the UI components (viewBindings references our UI components)
     */
    private fun setupUI() {
        val eventName = binding.editTextEventName
        val eventLocation = binding.editTextEventLocation
        val eventType = binding.editEventType
        val eventDescription = binding.editEventDescription
        val addEventButton = binding.fabAddEvent
        val eventDate = binding.fieldEventDate

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
                    eventDate = eventDate.editText?.text.toString().trim(),
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

        dateRangePicker.show(childFragmentManager, "DATE_RANGE_PICKER")

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

            val eventDate = binding.fieldEventDate
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
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}