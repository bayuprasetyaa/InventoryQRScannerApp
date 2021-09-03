package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityCreateBinding
import com.ikm.inventoryqrscanner.fragment.DateFragment
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.util.intChecker
import com.ikm.inventoryqrscanner.util.stringToDate
import com.ikm.inventoryqrscanner.util.stringToTimestamp
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private val db by lazy { Firebase.firestore }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.btnDelete.isVisible = false

        setupListener() // Fungsi Button (Listener)
    }

    private fun setupListener() {

        // Button Edit Field
        binding.calender.setOnClickListener {
            DateFragment(object : DateFragment.DateListener {
                override fun onSuccess(date: String) {
                    binding.editDate.setText(date)
                }

            }).apply { show(supportFragmentManager, "dateFragment")}
        }


        //Button Save
        binding.btnSave.setOnClickListener {

            if (!binding.editId.text.isNullOrEmpty() && !binding.editProduct.text.isNullOrEmpty()){
                sendData()
            }else{
                Toast.makeText(this, "Id Produk dan Nama Produk Harus di isi !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi kirim data
    private fun sendData() {


        // Ambil Data dari Edit Text

        val product = Product(
            number = binding.editId.text.toString(),
            product = binding.editProduct.text.toString(),
            expDate = stringToTimestamp(binding.editDate.text.toString()),
            amount = binding.editAmount.text.toString(),
            type = binding.editType.selectedItem.toString(),
            location = binding.editLocation.text.toString(),
            condition = binding.editCondition.selectedItem.toString(),
            description = binding.editDesc.text.toString())

        // Buat data
        db.collection("item_description")
            .document(product.number!!)
            .set(product)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot added with ID: ${product.number}")
                Toast.makeText(applicationContext, "Produk Berhasil Ditambahkan !", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error", e)
            }
    }

}