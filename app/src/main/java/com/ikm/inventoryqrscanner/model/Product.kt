package com.ikm.inventoryqrscanner.model
import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.*

data class Product(
    var id: String?,
    var number: Int,
    var product: String,
    var expDate: Timestamp?,
    var amount: Int,
    var type: String,
    var location: String,
    var condition: String,
    var description: String,
) : Serializable