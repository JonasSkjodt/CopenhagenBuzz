package dk.itu.moapd.copenhagenbuzz.skjo.view.fragments

import androidx.fragment.app.Fragment

/**
 * , (2) another for showing user-favorite events
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentCalendarBinding
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // Make sure to clear the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


/*class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

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
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        // Initialize your adapter with an empty list or initial data
        eventAdapter = EventAdapter(emptyList())

        // Set up your RecyclerView
        with(binding.listView) {
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
            Event("Cph Festival", "Copenhagen Downtown", "Fri, Feb 02 2024 - Sun, Feb 25 2024", "Festival", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium."),
            Event("Event 2", "Location 2", "Date 2", "Type 2", "Description 2"),
            Event("Event 3", "Location 3", "Date 3", "Type 3", "Description 3"),
            Event("Event 4", "Location 4", "Date 4", "Type 4", "Description 4"),
            Event("Event 5", "Location 5", "Date 5", "Type 5", "Description 5")
        )

        // Create a list to hold 50 events, repeating the test data
        val repeatedTestData = List(50) { i -> testData[i % testData.size] }

        // Update the adapter with the repeated test data
        eventAdapter.updateEvents(repeatedTestData)
    }


    // Make sure to clear the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/