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
 * TimelineFragment displays a timeline of events using a ListView.
 * It uses DataViewModel to fetch and observe event data, updating the UI as necessary.
 */
class TimelineFragment : Fragment() {

    private var _binding: FragmentTimelineBinding? = null

    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var eventAdapter: EventAdapter
    private lateinit var viewModel: DataViewModel

    /**
     * Inflates the layout for this fragment, initializes view binding, and sets up the timeline ListView.
     *
     * @param inflater Inflates the layout for this fragment.
     * @param container The parent view the fragment's UI should be attached to.
     * @param savedInstanceState Contains data supplied by the system if the fragment is being re-created.
     * @return The root View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }
    /**
     * Completes the initialization process of the ListView adapter and ViewModel.
     * Subscribes to ViewModel LiveData for events and loading status updates.
     *
     * @param view The View returned by onCreateView().
     * @param savedInstanceState If non-null, contains data supplied by the system if the fragment is being re-created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        // note: this must be set as the same in timeline fragment and favorite fragment
        // this is because ViewModelProvider(requireActivity()) ties the fragments to the same DataViewModel
        // where ViewModelProvider(this) each gets a separate DataViewModel instance
        // e.g. when you use "this", you create a new ViewModelProvider scoped to that specific fragment

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
    /**
     * Cleans up the binding when the Fragment's view is being destroyed to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

