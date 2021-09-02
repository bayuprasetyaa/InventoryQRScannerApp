package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import android.os.Bundle
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.adapter.ItemAdapter
import com.ikm.inventoryqrscanner.databinding.ActivityHomeBinding
import com.ikm.inventoryqrscanner.model.Product

class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val db by lazy { Firebase.firestore }
    private lateinit var adapter : ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupList()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        listItems()
    }

    private fun setupBinding() {
        setContentView( binding.root )
    }

    private fun setupList(){
        adapter = ItemAdapter(arrayListOf(), object : ItemAdapter.AdapterListener{
            override fun onClick(items: Product) {
                startActivity(
                    Intent(this@HomeActivity, UpdateActivity::class.java)
                        .putExtra("id", items.id)
                )
            }

            override fun onLongClick(items: Product) {

            }

        })
        binding.listItem.adapter = this.adapter
    }

    private fun setupListener(){

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }

        binding.fabList.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))}

    }

    private fun listItems() {
        db.collection("item_description")
            .orderBy("product")
            .limit(3)
            .get()
            .addOnSuccessListener{ result -> setProduct(result) }
    }

    private fun setProduct(result: QuerySnapshot) {
        val items: ArrayList<Product> = arrayListOf()
        for (document in result){
            items.add(
                Product(
                    id = document.reference.id,
                    number = document.data["number"].toString().toInt(),
                    product = document.data["product"].toString(),
                    expDate = document.data["expDate"] as Timestamp,
                    amount = document.data["amount"].toString().toInt(),
                    type = document.data["type"].toString(),
                    location = document.data["location"].toString(),
                    condition = document.data["condition"].toString(),
                    description = document.data["description"].toString()
                )
            )
        }
        this.adapter.setData(items)

    }
}