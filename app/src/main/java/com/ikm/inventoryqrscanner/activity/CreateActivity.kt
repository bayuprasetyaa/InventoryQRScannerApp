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
import com.ikm.inventoryqrscanner.util.stringToDate
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private val db by lazy { Firebase.firestore }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnDelete.isVisible = false
        setupListener()
    }

    private fun setupListener() {

        binding.editDate.setOnClickListener {
            DateFragment(object : DateFragment.DateListener {
                override fun onSuccess(date: String) {
                    binding.editDate.setText(date)
                }

            }).apply { show(supportFragmentManager, "dateFragment")}
        }

        binding.btnSave.setOnClickListener {

            val date = binding.editDate.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val product = Product(
                null,
                number = binding.editId.text.toString().toInt(),
                product = binding.editProduct.text.toString(),
                expDate = Timestamp(dateFormat.parse(date)),
                amount = binding.editAmount.text.toString().toInt(),
                type = binding.editType.text.toString(),
                location = binding.editLocation.text.toString(),
                condition = binding.editCondition.text.toString(),
                description = binding.editDesc.text.toString()
            )

            db.collection("item_description")
                .add(product)
                .addOnSuccessListener { documentReference ->
                    Log.e(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(applicationContext, "Produk Berhasil Ditambahkan !", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error", e)
                }

        }
    }


}