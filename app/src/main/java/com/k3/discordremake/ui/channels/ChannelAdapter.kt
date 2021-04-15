package com.k3.discordremake.ui.channels

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.k3.discordremake.R
import com.k3.discordremake.data.model.Channel
import com.k3.discordremake.ui.chat.FireStoreAdapter
import java.util.*

class ChannelAdapter(query: Query, private val listener: OnChannelSelectedListener): FireStoreAdapter<ChannelAdapter.ChannelViewHolder>(query) {

    interface OnChannelSelectedListener {
        fun onChannelSelected(channel: Channel?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.channel_item, parent, false)

        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject(Channel::class.java), listener)
    }

    inner class ChannelViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(channel: Channel?, listener: OnChannelSelectedListener) = with(view) {
            channel?.let {

                findViewById<EditText>(R.id.channelNameTextView).setText(it.name)
                findViewById<EditText>(R.id.descriptionTextView).setText(it.description)


                val rnd = Random()
                val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

                findViewById<ImageView>(R.id.channelIcon).setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
            }
            this.setOnClickListener {
                listener.onChannelSelected(channel)
            }
        }
    }

}