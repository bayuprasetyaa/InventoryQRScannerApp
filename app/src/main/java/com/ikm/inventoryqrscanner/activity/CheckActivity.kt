package com.ikm.inventoryqrscanner.activity

import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityProductBinding
import com.ikm.inventoryqrscanner.model.Product

class CheckActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }
    val number by lazy { intent.getStringExtra("number") }
    private val binding by lazy { ActivityProductBinding.inflate(layoutInflater) }
    private lateinit var items : Product

    override fun onStart() {
        super.onStart()
        detailProduct() // Fungsi untuk menampilkan detail produk
    }

    private fun detailProduct(){

    }
}