package com.ikm.inventoryqrscanner.model
import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class Product(
    var number: String? = "",
    var product: String? = "",
    var expDate: Timestamp? = null,
    var amount: String? = "",
    var type: String? = "",
    var location: String? = "",
    var condition: String? = "",
    var description: String? = "",
) : Serializable