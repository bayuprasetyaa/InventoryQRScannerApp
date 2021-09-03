package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R
import com.ikm.inventoryqrscanner.databinding.ActivityCreateBinding
import com.ikm.inventoryqrscanner.databinding.ActivityProductBinding
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.util.timestampToString
import java.text.SimpleDateFormat
import java.util.*

class ProductActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }
    val number by lazy { intent.getStringExtra("number") }
    private val binding by lazy { ActivityProductBinding.inflate(layoutInflater) }
    private lateinit var items : Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener() // Fungsi Button
    }

    override fun onStart() {
        super.onStart()
        detailProduct() // Fungsi untuk menampilkan detail produk
    }

    private fun setupListener() {
        // fungsi edit -> UpdateActivity
        binding.btnEdit.setOnClickListener {
            startActivity(
                Intent(this@ProductActivity, UpdateActivity::class.java)
                    .putExtra("number", items.number))
        }
    }

    private fun detailProduct() {

        // Get data from Firestore
        Log.e(TAG, "Input ${number}")

        db.collection("item_description")
            .document(number!!)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document["number"] != null) {
                        sendData(document) // Send data to firestore
                    } else {
                        sendMessage() // send message to Home ("Data tidak ditemukan !)"
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error getting documents.", exception) }
    }

    private fun sendData(document: DocumentSnapshot){
        items = Product(
            number = document["number"].toString(),
            product = document["product"].toString(),
            expDate = document["expDate"] as? Timestamp,
            amount = document["amount"].toString(),
            type = document["type"].toString(),
            location = document["location"].toString(),
            condition = document["condition"].toString(),
            description = document["description"].toString()
        )

        binding.id.text = items.number.toString()
        binding.product.text = items.product
        binding.date.text = timestampToString(items.expDate)
        binding.amount.text = items.amount.toString()
        binding.type.text = items.type
        binding.location.text = items.location
        binding.condition.text = items.condition
        binding.desc.text = items.description
    }

    private fun sendMessage(){

        val message = "Data Tidak Ditemukan !"

        startActivity(Intent(this, HomeActivity::class.java)
            .putExtra
                ("dataMessage", message))
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        super.onBackPressed()
    }
}