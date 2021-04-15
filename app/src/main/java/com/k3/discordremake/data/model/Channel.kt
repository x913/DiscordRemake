package com.k3.discordremake.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

// Firebase will ignore all properties which doesn`t match class properties
@IgnoreExtraProperties
class Channel {
    @DocumentId // this property is document id
    var id: String? = null

    var name: String? = null

    var description: String? = null

    constructor() {

    }

    constructor(id: String?, name: String?, description: String?) {
        this.id = id
        this.name = name
        this.description = description
    }

}