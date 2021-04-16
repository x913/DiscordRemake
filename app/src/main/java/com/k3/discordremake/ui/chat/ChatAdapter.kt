package com.k3.discordremake.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.k3.discordremake.R
import com.k3.discordremake.data.model.Channel
import com.k3.discordremake.data.model.Message
import java.text.SimpleDateFormat
import java.util.*

open class ChatAdapter(query: Query): FireStoreAdapter<ChatAdapter.MessageViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject(Message::class.java))
    }

    inner class MessageViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(message: Message?) = with(view) {
            message?.let {
                val pattern = context.getString(R.string.date_format)
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                val date: String = simpleDateFormat.format(it.timestamp)

                findViewById<TextView>(R.id.messageTextView).text = it.text
                findViewById<TextView>(R.id.timestampTextView).text = date
                findViewById<TextView>(R.id.senderTextView).text = it.userName
            }
        }
    }
}