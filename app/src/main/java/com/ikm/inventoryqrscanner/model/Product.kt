package com.ikm.inventoryqrscanner.model
import com.google.firebase.Timestamp
import java.io.Serializable

data class Product(
    var number: String? = "",
    var product: String? = "",
    var expDate: Timestamp?,
    var amount: String? = "",
    var type: String? = "",
    var location: String? = "",
    var condition: String? = "",
    var description: String? = "",
    var created: Timestamp? = Timestamp.now(),
    var count: Int,
) : Serializable