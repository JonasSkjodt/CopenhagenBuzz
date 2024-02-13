package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityLoginBinding

/**
 * LoginActivity handles (placeholder) user and guest login
 *
 * This activity provides the user interface for logging in. Users have the option to log in either
 * as a registered user or as a guest. When the user has logged in, it navigates to MainActivity.
 *
 * @constructor Creates an instance of LoginActivity.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //kotlin's "with" attribute for easier listener code readability
        with(binding) {
            userLoginButtonFront.setOnClickListener {
                navigateToMainActivity( isLoggedIn = true)
            }
            guestLoginButtonFront.setOnClickListener {
                navigateToMainActivity( isLoggedIn = false)
            }
        }
    }
    // navigateToMainActivity
    // uses Intent to navigate to main activity and sets boolean "isLoggedIn"
    // @see intent https://developer.android.com/reference/kotlin/android/content/Intent
    private fun navigateToMainActivity(isLoggedIn: Boolean) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("isLoggedIn", isLoggedIn)
        }
        startActivity(intent)
    }
}

