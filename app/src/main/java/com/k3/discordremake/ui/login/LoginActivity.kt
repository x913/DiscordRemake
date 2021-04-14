package com.k3.discordremake.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.k3.discordremake.R
import com.k3.discordremake.databinding.ActivityMainBinding
import com.k3.discordremake.ui.channels.ChannelsActivity
import org.jetbrains.anko.intentFor

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_main)
        setupLogin()
    }

    private fun setupLogin() {
        binding.signInButton.setOnClickListener {
            startActivity(intentFor<ChannelsActivity>())
        }
    }
}