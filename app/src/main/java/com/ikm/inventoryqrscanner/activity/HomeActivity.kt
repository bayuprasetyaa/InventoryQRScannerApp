package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.adapter.ItemAdapter
import com.ikm.inventoryqrscanner.databinding.ActivityHomeBinding
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.preferences.PreferenceManager
import kotlin.system.exitProcess

class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
//    private val dataMessage by lazy { intent.getStringExtra("dataMessage") }
    private val db by lazy { Firebase.firestore }
    private lateinit var adapter : ItemAdapter
    private var backPressedTime:Long = 0
    lateinit var backToast:Toast
    private val preference by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupBinding()
        setupList()
        setupListener()
        message()
    }

    override fun onStart() {
        super.onStart()
        listItems()
        checkUser()
    }

    private fun setupView(){
        binding.fabAdd.visibility = View.GONE
    }

    private fun setupBinding() {
        setContentView( binding.root )
    }

    private fun setupList(){
        adapter = ItemAdapter(arrayListOf(), object : ItemAdapter.AdapterListener{
            override fun onClick(items: Product) {
                startActivity(
                    Intent(this@HomeActivity, ProductActivity::class.java)
                        .putExtra("number", items.number)
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
            startActivity(Intent(this, ListActivity::class.java))
        }

        binding.scan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
            finish()
        }

        binding.searchBtn.setOnClickListener {
            val message = binding.search.text.toString()
            startActivity(Intent(this, ListActivity::class.java)
                .putExtra("search", message))
            Log.e(TAG, "input ${message}")
            finish()
        }

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
                    number = document.data["number"].toString(),
                    product = document.data["product"].toString(),
                    expDate = document.data["expDate"] as? Timestamp,
                    amount = document.data["amount"].toString(),
                    type = document.data["type"].toString(),
                    location = document.data["location"].toString(),
                    condition = document.data["condition"].toString(),
                    description = document.data["description"].toString()
                )
            )
        }
        this.adapter.setData(items)

    }

    private fun message(){
        if (intent.hasExtra("dataMessage")){
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        backToast = Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG)
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            this.finishAffinity();
            System.exit(0);
            super.onBackPressed()
        }else{
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun checkUser(){
        if (preference.getBoolean("admin") == true){
            binding.fabAdd.visibility = View.VISIBLE
        }
    }
}