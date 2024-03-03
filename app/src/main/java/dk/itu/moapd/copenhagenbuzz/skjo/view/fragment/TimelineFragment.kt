package dk.itu.moapd.copenhagenbuzz.skjo.view.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentTimelineBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.DataViewModel
import dk.itu.moapd.copenhagenbuzz.skjo.view.adapter.EventAdapter

/**
 * The TimelineFragment class displays events in the Copenhagen area
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        // note to self: this must be set as the same in timeline fragment and favorite fragment
        // this is because ViewModelProvider(requireActivity()) ties the fragments to the same DataViewModel
        // where ViewModelProvider(this) each gets a separate DataViewModel instance
        viewModel = ViewModelProvider(requireActivity()).get(DataViewModel::class.java)

        eventAdapter = EventAdapter(emptyList(), requireContext(), viewModel)
        binding.listView.adapter = eventAdapter

        viewModel.events.observe(viewLifecycleOwner) { events ->
            // Update the EventAdapter with the new events
            eventAdapter.updateEvents(events)
        }
        // show/hide the loading indicator (binding to loadingTextView in fragment_timeline.xml)
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingTextView.visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    // Make sure to clear the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

