package com.k3.discordremake.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@IgnoreExtraProperties
class Message {

    var text: String? = null

    var userId: String? = null

    var userName: String? = null

    @ServerTimestamp
    var timestamp: Date? = null

    constructor()

    constructor(text: String?, userId: String?, userName: String?, timestamp: Date?) {
        this.userId = userId
        this.text = text
        this.userName = userName
        this.timestamp = timestamp
    }

}