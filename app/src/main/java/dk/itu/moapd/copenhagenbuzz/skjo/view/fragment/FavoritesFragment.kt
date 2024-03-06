package dk.itu.moapd.copenhagenbuzz.skjo.view.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentFavoritesBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.DataViewModel
import dk.itu.moapd.copenhagenbuzz.skjo.view.adapter.FavoritesAdapter

/**
 * FavoritesFragment is responsible for displaying the user's favorite events.
 * It holds a reference to a DataViewModel to observe changes to the favorites list and updates the favorites UI accordingly.
 */
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var viewModel: DataViewModel

    /**
     * Inflates the layout for this fragment, initializes view binding, and sets up the favorites RecyclerView.
     *
     * @param inflater Inflates the layout for this fragment.
     * @param container The parent view the favorites fragment's UI should be attached to.
     * @param savedInstanceState Contains data supplied by the system if the fragment is being re-created.
     * @return The root View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }
    /**
     * Completes the initialization process of the RecyclerView adapter and ViewModel.
     * Subscribes to ViewModel LiveData to be notified of changes in the favorites list.
     *
     * @param view The View returned by onCreateView().
     * @param savedInstanceState If non-null, contains data supplied by the system if the fragment is being re-created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel (this must be set as the same in timeline fragment and favorite fragment)
        viewModel = ViewModelProvider(requireActivity()).get(DataViewModel::class.java)

        // Initialize the adapter with an empty list or initial data
        favoritesAdapter = FavoritesAdapter(emptyList())

        // Set up the RecyclerView
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }

        //liveData of favorites from ViewModel
        viewModel.favorites.observe(viewLifecycleOwner) { favoriteEvents ->
            // Update the adapter with the repeated test data
            //cehck if it actually populates the list
            Log.d("FavoritesFragment", "Observed favorites: ${favoriteEvents.joinToString { it.eventName }}")
            favoritesAdapter.updateEvents(favoriteEvents)
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