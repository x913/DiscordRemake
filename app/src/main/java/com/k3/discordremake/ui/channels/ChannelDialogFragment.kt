package com.k3.discordremake.ui.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.k3.discordremake.R
import com.k3.discordremake.data.model.Channel

class ChannelDialogFragment : DialogFragment(), View.OnClickListener {

    internal interface ChannelListener {
        fun onChannel(channel: Channel)
    }

    lateinit var channelName: EditText
    lateinit var channelDescription: EditText

    private var channelListener: ChannelListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dialog_channel, container, false)

        v.findViewById<View>(R.id.submit_channel_button).setOnClickListener(this)
        v.findViewById<View>(R.id.cancel_channel_button).setOnClickListener(this)

        channelName = v.findViewById<EditText>(R.id.channel_name_text)
        channelDescription = v.findViewById<EditText>(R.id.channel_description_text)

        return v
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun onCancelClicked(view: View) {
        channelName.text.clear()
        channelDescription.text.clear();
        dismiss()
    }

    private fun onSubmitClicked(view: View) {
        val channel = Channel(
            null,
            channelName.text.toString(),
            channelDescription.text.toString()
        )

        channelName.text.clear()
        channelDescription.text.clear()
        channelListener?.onChannel(channel)
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ChannelListener) {
            channelListener = context
        }
    }

    companion object {
        const val TAG = "ChannelDialog"
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.submit_channel_button -> onSubmitClicked(v)
            R.id.cancel_channel_button -> onCancelClicked(v)
        }
    }

}