package dk.itu.moapd.copenhagenbuzz.skjo.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.skjo.R
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.ActivityLoginBinding

/**
 * LoginActivity ...
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val signInLauncher =
        registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result -> onSignInResult(result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLoginButtons()
    }

    private fun setupLoginButtons() {
        binding.buttonEmailLogin.setOnClickListener {
            // Launch FirebaseUI
            createSignInIntent()
        }
        binding.buttonGuestLogin.setOnClickListener {
            // Guest Login
            signInAnonymously()
        }
    }

    private fun signInAnonymously() {
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success, update UI with the signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    /* TODO guest user to be deleted after logout?
    private fun deleteAnonymousUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && user.isAnonymous) {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Anonymous user deleted
                }
            }
        }
    }*/

    private fun createSignInIntent() {
    // Choose authentication providers.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            //AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
        // Create and launch sign-in intent.
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setLogo(R.drawable.applogoround)
            .setTheme(R.style.Theme_FirebaseAuthentication)
            .apply {
                setTosAndPrivacyPolicyUrls(
                    "https://firebase.google.com/terms/",
                    "https://firebase.google.com/policies/…"
                )
            }
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(
        result: FirebaseAuthUIAuthenticationResult
    ) {
        when (result.resultCode) {
            RESULT_OK -> {
                // Successfully signed in.
                Toast.makeText(binding.root.context, "You successfully logged in", Toast.LENGTH_SHORT).show()
                startMainActivity()
            }
            else -> {
                // Sign in failed.
                Toast.makeText(binding.root.context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}
