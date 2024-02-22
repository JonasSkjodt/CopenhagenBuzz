package dk.itu.moapd.copenhagenbuzz.skjo.view

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentAddEventBinding
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentFavoritesBinding
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentTimelineBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

/**
 * (1) one for displaying the next events
 * in the Copenhagen area
 */
class TimelineFragment : Fragment() {

    private var _binding: FragmentTimelineBinding? = null

    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)

        // Initialize your adapter with an empty list or initial data
        eventAdapter = EventAdapter(emptyList())

        // Set up your RecyclerView
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        return binding.root
    }

    // Add a function to update the RecyclerView when a new event is added
    /*fun addEvent(event: Event) {
        val currentEvents = eventAdapter.eventsList.toMutableList()
        currentEvents.add(event)
        eventAdapter.updateEvents(currentEvents)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with an empty list or initial data
        eventAdapter = EventAdapter(emptyList())

        // Set up the RecyclerView
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        // Add some test data to the adapter for debugging purposes
        val testData = listOf(
            Event("Event 1", "Location 1", "Date 1", "Type 1", "Description 1"),
            Event("Event 2", "Location 2", "Date 2", "Type 2", "Description 2"),
            Event("Event 3", "Location 3", "Date 3", "Type 3", "Description 3"),
            Event("Event 4", "Location 4", "Date 4", "Type 4", "Description 4"),
            Event("Event 5", "Location 5", "Date 5", "Type 5", "Description 5")
        )
        eventAdapter.updateEvents(testData)
    }


    // Make sure to clear the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

