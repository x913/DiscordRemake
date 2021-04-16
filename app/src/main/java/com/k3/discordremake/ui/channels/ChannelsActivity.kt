package com.k3.discordremake.ui.channels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.k3.discordremake.R
import com.k3.discordremake.data.model.Channel
import com.k3.discordremake.databinding.ActivityChannelsBinding
import com.k3.discordremake.ui.chat.ChatActivity
import com.k3.discordremake.ui.login.LoginActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar

class ChannelsActivity : AppCompatActivity(), AnkoLogger, ChannelAdapter.OnChannelSelectedListener, ChannelDialogFragment.ChannelListener {

    private lateinit var binding: ActivityChannelsBinding
    private lateinit var channelDialogFragment: ChannelDialogFragment
    private lateinit var firestore: FirebaseFirestore
    private lateinit var query: Query
    private lateinit var adapter: ChannelAdapter
    private lateinit var channelReference: DocumentReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ChannelsActivity, R.layout.activity_channels)
        channelDialogFragment = ChannelDialogFragment()
        auth = FirebaseAuth.getInstance()
        initFireStore()
        initRecyclerView()
    }

    private fun addChannel(channel: Channel): Task<Void> {
        return firestore.runTransaction {
            it[channelReference] = channel
            null
        }
    }

    fun showChannelDialog(view: View) {
        channelDialogFragment.show(supportFragmentManager, ChannelDialogFragment.TAG)
    }

    private fun initFireStore() {
        firestore = FirebaseFirestore.getInstance()
        channelReference = firestore.collection(CHANNELS).document()
        query = firestore
            .collection(CHANNELS)
            .orderBy(NAME, Query.Direction.DESCENDING)
    }

    private fun initRecyclerView() {
        adapter = object : ChannelAdapter(query, this@ChannelsActivity) {

            override fun onError(e: FirebaseFirestoreException) {
                binding.channelsLayout.longSnackbar(getString(R.string.network_error))
                debug("FireStore exception: ${e?.message}")
            }

            override fun onDataChanged() {
                if(itemCount == 0) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.channelsRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.channelsRecyclerView.visibility = View.VISIBLE
                }
                binding.channelsRecyclerView.layoutManager = LinearLayoutManager(this@ChannelsActivity)
                binding.channelsRecyclerView.adapter = adapter
            }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        private const val CHANNELS = "channels"
        private const val NAME = "name"
    }

    override fun onChannelSelected(channel: Channel?) {
        channel?.let {
            startActivity(intentFor<ChatActivity>(ChatActivity.KEY_CHANNEL_ID to it.id, ChatActivity.KEY_CHANNEL_NAME to it.name))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                auth.signOut()
                startActivity(intentFor<LoginActivity>().clearTask().newTask())
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onChannel(channel: Channel) {
        Log.d("AAA", "adding new channel")
        if(!channel.name.isNullOrEmpty() && !channel.description.isNullOrEmpty()) {
            addChannel(channel)
                .addOnSuccessListener {
                    longToast("Channel Created")
                }
                .addOnFailureListener {
                    binding.channelsLayout.longSnackbar(getString(R.string.network_error))
                }
        } else{
            binding.channelsLayout.longSnackbar(getString(R.string.name_or_description_empty))
        }
    }
}