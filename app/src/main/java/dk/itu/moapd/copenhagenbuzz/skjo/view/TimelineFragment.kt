package dk.itu.moapd.copenhagenbuzz.skjo.view

import androidx.fragment.app.Fragment

/**
 * (1) one for displaying the next events
 * in the Copenhagen area
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FragmentTimelineBinding

class TimelineFragment : Fragment() {

    private var _binding: FragmentTimelineBinding? = null

    //!! will throw NullPointerException if the value is null.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
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

