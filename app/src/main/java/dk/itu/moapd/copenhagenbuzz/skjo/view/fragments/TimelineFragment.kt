package dk.itu.moapd.copenhagenbuzz.skjo.view.fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentTimelineBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.DataViewModel
import dk.itu.moapd.copenhagenbuzz.skjo.view.EventAdapter

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
    private lateinit var viewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        // Initialize EventAdapter with an empty list
        eventAdapter = EventAdapter(emptyList(), requireContext())
        // Set the EventAdapter to the ListView.
        binding.listView.adapter = eventAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LiveData containing the list of events
        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        viewModel.events.observe(viewLifecycleOwner) { events ->
            // Update the EventAdapter with the new events
            eventAdapter.updateEvents(events)
        }

    }

    // Make sure to clear the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

