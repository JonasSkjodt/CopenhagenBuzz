package dk.itu.moapd.copenhagenbuzz.skjo.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.skjo.R
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.HeaderNavigationDrawerBinding


class UserInfoFragment : Fragment() {

    private var _headerBinding: HeaderNavigationDrawerBinding? = null

    private val headerBinding
        get() = requireNotNull(_headerBinding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            // Check if the fragment's view is still around when updating.
            if (isAdded) {
                updateNavigationHeader(firebaseAuth.currentUser)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        _headerBinding = HeaderNavigationDrawerBinding.bind(navView.getHeaderView(0))
        // Call updateNavigationHeader here to make sure the header is updated as soon as the view is created.
        updateNavigationHeader(auth.currentUser)
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }


    fun updateNavigationHeader(user: FirebaseUser?) {
        user?.takeIf { !it.isAnonymous }?.let { nonAnonymousUser ->
            headerBinding.headerNavTextUserName.text = nonAnonymousUser.displayName ?: getString(R.string.navigation_user_name)
            headerBinding.headerNavTextUserEmail.text = nonAnonymousUser.email ?: getString(R.string.navigation_user_email)
            nonAnonymousUser.photoUrl?.let { url ->
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.round_account_circle_24)
                    .error(R.drawable.round_account_circle_24)
                    .into(headerBinding.headerNavImageUserPhoto)
            } ?: run {
                // Set a default image or make it invisible if there is no photo URL.
                headerBinding.headerNavImageUserPhoto.setImageResource(R.drawable.round_account_circle_24)
            }
        } ?: run {
            // Handle the case where there is no user logged in or user is anonymous.
            headerBinding.headerNavTextUserName.text = "Guest user"
            headerBinding.headerNavTextUserEmail.text = "guest@email.com"
            headerBinding.headerNavImageUserPhoto.setImageResource(R.drawable.round_account_circle_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _headerBinding = null
    }
}


