package dk.itu.moapd.copenhagenbuzz.skjo.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.skjo.R
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.DialogUserInfoBinding

class UserInfoDialogFragment : Fragment() {

    private var _binding: DialogUserInfoBinding? = null

    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Get the current user.
        val currentUser = FirebaseAuth.getInstance().currentUser
        // Populate the dialog view with user information.
        currentUser?.let { user ->
            with(binding) {
                textViewName.text = user.displayName ?: ""
                textViewEmail.text = user.email ?: ""
                user.photoUrl?.let { url ->
                    imageViewPhoto.imageTintMode = null
                    Picasso.get().load(url).into(imageViewPhoto)
                }
            }
        } ?: run {
            // Handle case where no user is signed in (e.g., display message)
            binding.textViewName.text = getString(R.string.not_logged_in)
            binding.textViewEmail.text = ""
            binding.imageViewPhoto.visibility = View.GONE // Hide photo view
        }

        // Create and return a new dialog.
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.user_info_title)
            .setView(binding.root)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}