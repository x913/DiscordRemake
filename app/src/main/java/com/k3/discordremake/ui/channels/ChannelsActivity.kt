package com.k3.discordremake.ui.channels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.k3.discordremake.R

class ChannelsActivity : AppCompatActivity() {

    lateinit var channelDialogFragment: ChannelDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)
        channelDialogFragment = ChannelDialogFragment()
    }

    fun showChannelDialog(view: View) {

        channelDialogFragment.show(supportFragmentManager, ChannelDialogFragment.TAG)
    }
}