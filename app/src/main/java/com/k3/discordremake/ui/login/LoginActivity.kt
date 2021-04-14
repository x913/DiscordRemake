package com.k3.discordremake.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.k3.discordremake.R
import com.k3.discordremake.databinding.ActivityMainBinding
import com.k3.discordremake.ui.channels.ChannelsActivity
import com.k3.discordremake.ui.signup.SignUpActivity
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_main)
        if(auth.currentUser != null) {
            startActivity(intentFor<ChannelsActivity>())
        } else {
            setupLogin()
            setupSignUp()
            setupTextFields()
            setupForgotPassword()
        }
    }

    private fun setupLogin() {
        binding.signInButton.setOnClickListener {

            when {
                !isEmailValid(binding.emailTextView.text.toString()) -> {
                    binding.emailSignUpTextInputLayout.error = "Email is not valid"
                }
                !isPasswordValid(binding.passwordTextView.text.toString()) -> {
                    binding.emailPasswordTextInputLayout.error = "Password is not valid"
                }
                else -> {
                    binding.loadingProgress.visibility = View.VISIBLE
                    auth.signInWithEmailAndPassword(binding.emailTextView.text.toString(), binding.passwordTextView.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful && it.isComplete) {
                            startActivity(intentFor<ChannelsActivity>())
                            longToast("Welcome ${auth.currentUser?.displayName}")
                        } else {
                            binding.loadingProgress.visibility = View.GONE
                            binding.container.longSnackbar(it.exception?.localizedMessage.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setupForgotPassword() {
        binding.forgotTextView.setOnClickListener {
            forgotPassword(it)
        }
    }

    private fun setupSignUp() {
        binding.signUpButton.setOnClickListener {
            startSignUpActivity(it)
        }
    }

    private fun startSignUpActivity(view: View) {
        startActivity(intentFor<SignUpActivity>())
    }

    private fun forgotPassword(view: View) {
        if(!isEmailValid(binding.emailTextView.text.toString())) {
            binding.emailSignUpTextInputLayout.error = getString(R.string.email_not_valid)
        } else {
            auth.sendPasswordResetEmail(binding.emailTextView.text.toString()).addOnCompleteListener {
                if(it.isComplete && it.isSuccessful) {
                    binding.container.longSnackbar(getString(R.string.recover_email_sent))
                } else {
                    binding.container.longSnackbar(it.exception?.message.toString())
                }
            }
        }
    }

    private fun setupTextFields() {
        binding.emailTextView.doAfterTextChanged {
            if(it != null) {
                binding.emailTextView.error = null
            }
        }

        binding.passwordTextView.doAfterTextChanged {
            if(isPasswordValid(it.toString())) {
                binding.passwordTextView.error = null
            }
        }

    }

    private fun isEmailValid(email: String): Boolean = email.contains('@') && email.isNotBlank()

    private fun isPasswordValid(password: String): Boolean = password.length >= 6

}