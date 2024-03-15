package dk.itu.moapd.copenhagenbuzz.skjo.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
     * Sets up the current layout
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

        // Add a listener to intercept navigation to AddEventFragment (so the user can't access addeventfragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.addeventFragment) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null && !user.isAnonymous) {
                    // User is signed in and not a guest, allow navigation
                } else {
                    // Navigate to timeline fragment if its a guest
                    navController.navigate(R.id.timelineFragment)
                }
            }
        }

        // Set up the DrawerLayout and ActionBarDrawerToggle so the sidebar can actually slide open
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.topAppBar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Setup ActionBar with NavController and DrawerLayout
        /*appBarConfiguration = AppBarConfiguration(setOf(
            R.id.timelineFragment, R.id.favoriteFragment, R.id.mapsFragment, R.id.calendarFragment
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Setup NavigationView with NavController
        val navigationView: NavigationView = binding.navView
        navigationView.setupWithNavController(navController)*/

        //remembers if the user is logged in or not
        if (savedInstanceState == null) {
            viewModel.isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
        }

    }
    private fun updateDrawerMenu(user: FirebaseUser?) {
        val navigationView = binding.navView
        val menu = navigationView.menu
        val userItems = menu.findItem(R.id.navigation_user_items) // The ID for the user specific menu items (drawer_menu.xml)
        userItems.isVisible = user != null && !user.isAnonymous // Show the items if the user is logged in and hide them if not
    }

    //firebase
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateDrawerMenu(currentUser)
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

    //firebase user info and log out in topappbar menu
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