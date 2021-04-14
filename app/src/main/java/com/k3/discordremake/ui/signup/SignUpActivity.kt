package com.k3.discordremake.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.k3.discordremake.R
import com.k3.discordremake.databinding.ActivitySignUpBinding
import com.k3.discordremake.ui.channels.ChannelsActivity
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.intentFor

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)
        setupSignUp()
    }

    private fun setupSignUp() {
        binding.signUpButton.setOnClickListener {
            when {
                !isEmailValid(binding.emailTextInput.text.toString()) -> {
                    binding.emailSignUpTextInputLayout.error = getString(R.string.email_not_valid)
                }

                !isPasswordValid(binding.firstPasswordText.text.toString(), binding.secondPasswordText.text.toString()) -> {
                    binding.firstPasswordSignUpTextInputLayout.error = getString(R.string.password_not_valid_for_registration)
                }

                !isUserNameValid(binding.userNameTextInput.text.toString()) -> {
                    binding.userNameTextInputLayout.error = getString(R.string.user_name_not_valid)
                }

                else -> {
                    // sign up user
                    binding.signUpProgress.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(binding.emailTextInput.text.toString(), binding.firstPasswordText.text.toString()).addOnCompleteListener {
                        binding.signUpProgress.visibility = View.GONE
                        if(it.isSuccessful && it.isComplete) {
                            val profileRequestUpdate = UserProfileChangeRequest.Builder()
                                .setDisplayName(binding.userNameTextInput.text.toString())
                                .build()

                            auth.currentUser?.updateProfile(profileRequestUpdate)
                            startActivity(intentFor<ChannelsActivity>())

                        } else {
                            binding.container.longSnackbar(it.exception?.localizedMessage.toString())
                        }
                    }
                }

            }
        }
    }


    private fun isUserNameValid(userName: String): Boolean = userName.isNotBlank()

    private fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String, secondaryPassword: String): Boolean =
        password.isNotBlank() &&
                secondaryPassword.isNotBlank() &&
                password == secondaryPassword &&
                password.length >= 6

}