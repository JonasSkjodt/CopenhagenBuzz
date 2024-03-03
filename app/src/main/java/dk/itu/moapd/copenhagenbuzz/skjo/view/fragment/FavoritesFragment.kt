package dk.itu.moapd.copenhagenbuzz.skjo.view.fragment

import androidx.fragment.app.Fragment

/**
 * The FavoritesFragment class shows user-favorite events
 */
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

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var viewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

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

    // Make sure to clear the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}