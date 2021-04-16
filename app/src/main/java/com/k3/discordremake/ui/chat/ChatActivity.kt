package com.k3.discordremake.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.k3.discordremake.R
import com.k3.discordremake.databinding.ActivityChatBinding
import org.jetbrains.anko.design.longSnackbar
import java.lang.IllegalArgumentException

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var channelRef: DocumentReference
    private lateinit var messagesQuery: Query
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ChatActivity, R.layout.activity_chat)
        val channelId = intent.extras!!.getString(KEY_CHANNEL_ID) ?: throw IllegalArgumentException("Must pass extra $KEY_CHANNEL_ID")
        title = intent.extras!!.getString(KEY_CHANNEL_NAME)
        initFirestore(channelId)
        initRecyclerView()
    }


    private fun initFirestore(channelId: String) {
        firestore = FirebaseFirestore.getInstance()

        channelRef = firestore
            .collection(CHANNEL_COLLECTION)
            .document(channelId)

        messagesQuery = channelRef
            .collection(MESSAGE_COLLECTION)
            .orderBy(TIMESTAMP, Query.Direction.ASCENDING)
    }

    private fun initRecyclerView() {
        Log.d("AAA", "Creating Chat Adapter")
        adapter = object : ChatAdapter(messagesQuery) {
            override fun onError(e: FirebaseFirestoreException) {
                binding.containerLayout.longSnackbar(e?.message.toString())
            }

            override fun onDataChanged() {
                Log.d("AAA", "onDataChanged")
                if(itemCount == 0) {
                    binding.chatEmptyView.visibility = View.VISIBLE
                    binding.messagesRecyclerView.visibility = View.GONE
                } else {
                    binding.chatEmptyView.visibility = View.GONE
                    binding.messagesRecyclerView.visibility = View.VISIBLE
                }
                val layoutManager = LinearLayoutManager(this@ChatActivity)
                layoutManager.stackFromEnd = true
                binding.messagesRecyclerView.layoutManager = layoutManager
                binding.messagesRecyclerView.adapter = adapter
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

    fun sendClicked(view: View) {

    }

    companion object {
        private const val LIMIT = 50
        const val KEY_CHANNEL_ID = "key_restaurant_id"
        const val KEY_CHANNEL_NAME = "key_channel_name"
        private const val MESSAGE_COLLECTION = "messages"
        private const val CHANNEL_COLLECTION = "channels"
        private const val TIMESTAMP = "timestamp"
    }
}