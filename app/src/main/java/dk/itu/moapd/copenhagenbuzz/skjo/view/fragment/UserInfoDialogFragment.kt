package dk.itu.moapd.copenhagenbuzz.skjo.view.fragment

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.skjo.R
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.DialogUserInfoBinding

class UserInfoDialogFragment : DialogFragment() {

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
        // Inflate the layout for this dialog
        val inflater = requireActivity().layoutInflater
        val view = DialogUserInfoBinding.inflate(inflater, null, false)

        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            view.textViewName.text = user.displayName ?: ""
            view.textViewEmail.text = user.email ?: ""
            user.photoUrl?.let { url ->
                view.imageViewPhoto.imageTintMode = null
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.applogoround) // Placeholder image.
                    .error(R.drawable.placeholderimg) // Image to show on error if the URL is invalid or on failure.
                    .into(view.imageViewPhoto)
            } ?: run {
                view.imageViewPhoto.visibility = View.GONE // Hide photo view if no URL
            }
        } ?: run {
            view.textViewName.text = getString(R.string.not_logged_in)
            view.textViewEmail.text = ""
            view.imageViewPhoto.visibility = View.GONE
        }

        // Create and return the dialog with the inflated view
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.user_info_title)
            .setView(view.root) // Use the inflated view here
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