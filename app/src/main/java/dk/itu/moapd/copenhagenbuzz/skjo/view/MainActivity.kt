package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.itu.moapd.copenhagenbuzz.skjo.R
import dk.itu.moapd.copenhagenbuzz.skjo.controller.MainViewModel

/**
 * The MainActivity class handles user interactions (like events) and initializes the UI in the app
 *
 * KDoc the code
 * @see (https://kotlinlang.org/docs/kotlin-doc.html#sample-identifier)
 * @see (https://source.android.com/docs/core/architecture/hidl/code-style)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var bottomNavigation : BottomNavigationView

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

        //make sure the toolbar is there (header menu)
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        bottomNavigation = findViewById(R.id.bottomNavigationView)

        if (savedInstanceState == null) {
            viewModel.isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
            loadFragment(AddEventFragment()) // Load default fragment
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.navigation_timeline -> TimelineFragment()
                R.id.navigation_favorites -> FavoritesFragment()
                R.id.navigation_maps -> MapsFragment()
                R.id.navigation_calendar -> CalendarFragment()
                else -> {
                    Log.w(TAG, "Unknown navigation item selected")
                    return@setOnItemSelectedListener false
                }
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        // look into using the navController here (slide 4, page 43)
        transaction.commit()
    }

    /**
     * Inflates the menu resource (defined in XML) into the Menu provided in the parameter.
     *
     * @param menu The options menu in which we place your items (currently linked to top_app_bar.xml).
     * @return Boolean Return true to display the menu; if we return false it will not be shown.
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
     * @return Boolean we must return true for the menu to be displayed; if we return false it will not be shown.
     */
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.user_account_item)?.isVisible = !viewModel.isLoggedIn
        menu.findItem(R.id.guest_account_item)?.isVisible = viewModel.isLoggedIn

        return super.onPrepareOptionsMenu(menu)
    }
    /**
     * This method is called whenever an item in the options menu is selected in the topAppBar.
     * The default returns false.
     * (for now it only handles guests being able to click back to the login page)
     *
     * @param item The menu item that was selected.
     * @return Boolean Return false to allow normal menu processing
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.user_account_item -> {
                // Intent to start LoginActivity
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                true
            }
            // Add more cases for other menu items as we go to the next exercises
            else -> super.onOptionsItemSelected(item)
        }
    }
}