package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.adapter.ItemAdapter
import com.ikm.inventoryqrscanner.databinding.ActivityListBinding
import com.ikm.inventoryqrscanner.model.Product

class ListActivity : BaseActivity() {

    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }
    private val db by lazy { Firebase.firestore }
    private val product by lazy { intent.getStringExtra("search") }
    private lateinit var adapter : ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
    }

    private fun setupList() {
        adapter = ItemAdapter(arrayListOf(), object : ItemAdapter.AdapterListener{
            override fun onClick(items: Product) {
                Log.e(TAG, "Send ${items.number}")
                startActivity(
                    Intent(this@ListActivity, ProductActivity::class.java)
                        .putExtra("number", items.number)
                )
            }
        })
        binding.listItem.adapter = this.adapter

    }

    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("search")){
            search()
        }else
            listItems()
    }

    private fun listItems() {
        db.collection("item_description")
            .orderBy("product")
            .get()
            .addOnSuccessListener{ result ->
                if (result.isEmpty){
                    binding.noData.visibility = View.VISIBLE
                }else{
                    binding.noData.visibility = View.GONE
                    setProduct(result)
                }
            }
    }

    private fun search(){
        Log.e(TAG, "output $product")
        db.collection("item_description")
            .whereEqualTo("product", product)
            .get()
            .addOnSuccessListener{ result ->
                if (result.isEmpty){
                    binding.noData.visibility = View.VISIBLE
                }else{
                    binding.noData.visibility = View.GONE
                    setProduct(result)
                }
            }
    }

    private fun setProduct(result: QuerySnapshot) {
        Log.e(TAG, "output $result")
        val items: ArrayList<Product> = arrayListOf()
        for (document in result){
            items.add(
                Product(
                    number = document.data["number"].toString(),
                    product = document.data["product"].toString(),
                    expDate = document.data["expDate"] as? Timestamp,
                    amount = document.data["amount"].toString(),
                    type = document.data["type"].toString(),
                    location = document.data["location"].toString(),
                    condition = document.data["condition"].toString(),
                    description = document.data["description"].toString(),
                    created = document.data["created"] as? Timestamp,
                    count = document.data["count"].toString().toInt()
                )
            )
        }
        this.adapter.setData(items)

    }
    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        super.onBackPressed()
    }
}