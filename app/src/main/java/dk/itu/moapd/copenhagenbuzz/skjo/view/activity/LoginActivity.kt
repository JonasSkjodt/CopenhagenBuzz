package dk.itu.moapd.copenhagenbuzz.skjo.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityLoginBinding

/**
 * LoginActivity handles user and guest login (note: for now placeholders are inserted until firebase is integrated)
 *
 * This activity provides the user interface for logging in. Users have the option to log in either
 * as a registered user or as a guest. When the user has logged in, it navigates to MainActivity.
 *
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    /**
     * SetupUI() Initializes the UI components and listeners (viewBindings references our UI components)
     */
    private fun setupUI() {
        with(binding) {
            userLoginButtonFront.setOnClickListener {
                navigateToMainActivity( isLoggedIn = true)
            }
            guestLoginButtonFront.setOnClickListener {
                navigateToMainActivity( isLoggedIn = false)
            }
        }
    }
    /**
     * navigateToMainActivity
     * uses Intent to navigate to main activity and sets boolean "isLoggedIn"
     *
     * @param Boolean whether the user is logged in or not
     *
     * @see intent https://developer.android.com/reference/kotlin/android/content/Intent
     */
    private fun navigateToMainActivity(isLoggedIn: Boolean) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("isLoggedIn", isLoggedIn)
        }
        startActivity(intent)
    }
}

