package dk.itu.moapd.copenhagenbuzz.skjo.view.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.WindowCompat
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.skjo.R
import dk.itu.moapd.copenhagenbuzz.skjo.model.MainViewModel
import dk.itu.moapd.copenhagenbuzz.skjo.view.fragment.UserInfoDialogFragment

/**
 * The MainActivity class handles user interactions (like events) and initializes the UI in the app
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    //firebase
    private lateinit var auth: FirebaseAuth

    // A set of private constants used in this class.
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    /**
     * viewModel
     * viewModel sorts rotation data bug with Android Jetpack
     *
     * @see (https://learnit.itu.dk/pluginfile.php/383364/mod_resource/content/0/Slides%20%2303.pdf)
     */
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    /**
     * Sets up the current layout, the view bindings, and listeners.
     * When the add event button is clicked, it validates the input fields and then creates this new event (if all fields are non-empty).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize Firebase auth.
        auth = FirebaseAuth.getInstance()

        //make sure the toolbar is there (header menu)
        setSupportActionBar(binding.topAppBar)

        // Retrieve NavController from NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        if (savedInstanceState == null) {
            viewModel.isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        }

    }
    //firebase
    override fun onStart() {
        super.onStart()
        // Redirect the user to the LoginActivity
        // if they are not logged in.
        auth.currentUser ?: startLoginActivity()
    }
    //firebase
    private fun startLoginActivity() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.let(::startActivity)
    }

    /**
     * Inflates the menu resource (defined in XML) into the Menu provided in the parameter.
     *
     * @param menu The options menu in which we place your items (currently linked to top_app_bar.xml).
     * @return Boolean Return true to display the menu; if its returned as false it will not be shown.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }
    /**
     * Prepares the screen's standard options menu to be displayed in the topAppBar.
     * It shows or hides the menu items based on whether the user is logged in or not.
     *
     * @param menu The options menu as last shown or first initialized by onCreateOptionsMenu().
     * @return Boolean must return true for the menu to be displayed; if its returned as false it will not be shown.
     *
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.user_account_item)?.isVisible = !viewModel.isLoggedIn
        menu.findItem(R.id.guest_account_item)?.isVisible = viewModel.isLoggedIn

        return super.onPrepareOptionsMenu(menu)
    }*/
    /**
     * This method is called whenever an item in the options menu is selected in the topAppBar.
     * The default returns false.
     * (for now it only handles guests being able to click back to the login page)
     *
     * @param item The menu item that was selected.
     * @return Boolean Return false to allow normal menu processing
     *
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.user_account_item -> {
                // Intent to start LoginActivity
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                true
            }
            // potetially add more cases for other menu items as we go to the next exercises...
            else -> super.onOptionsItemSelected(item)
        }
    }*/
    override fun onOptionsItemSelected(item: MenuItem):
            Boolean = when (item.itemId) {
        // Handle top app bar menu item clicks.
        R.id.action_user_info -> {
            UserInfoDialogFragment().apply {
                isCancelable = false
            }.also { dialogFragment ->
                dialogFragment.show(supportFragmentManager,
                    "UserInfoDialogFragment")
            }
            true
        }
        R.id.action_logout -> {
            auth.signOut()
            startLoginActivity()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}