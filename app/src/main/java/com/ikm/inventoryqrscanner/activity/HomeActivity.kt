package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    private lateinit var backToast:Toast
    private val preference by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        setupView()
        setupList() // list product function
        setupListener() // Listener function
        message() // message from other activity
    }

    override fun onStart() {
        super.onStart()
        listItems() // Receive data from database
        checkUser() // check user type
    }

    private fun setupView(){
        binding.fabAdd.visibility = View.GONE
    }

    private fun setupList(){

        // list the adapter
        adapter = ItemAdapter(arrayListOf(), object : ItemAdapter.AdapterListener{
            override fun onClick(items: Product) {
                startActivity(
                    Intent(this@HomeActivity, ProductActivity::class.java)
                        .putExtra("number", items.number)
                )
            }
        })

        binding.listItem.adapter = this.adapter
    }

    // Fungsi button
    private fun setupListener(){

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }

        binding.listButton.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        binding.scan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
            finish()
        }

        binding.searchButton.setOnClickListener {
            val message = binding.search.text.toString()
            startActivity(Intent(this, ListActivity::class.java)
                .putExtra("search", message))
            Log.e(TAG, "input $message")
            finish()
        }

    }

    //-------------- External Function ----------------//

    // Receive data from database
    private fun listItems() {
        db.collection("item_description")
            .orderBy("product")
            .limit(10) // Set max product to list
            .get()
            .addOnSuccessListener{ result ->

                if (result.isEmpty){
                    binding.noData.visibility = View.VISIBLE
                    binding.listButton.visibility = View.GONE
                }else{
                    binding.noData.visibility = View.GONE
                    binding.listButton.visibility = View.VISIBLE
                    setProduct(result)
                }
            }
    }

    // Put information from database to adapter
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

    // Check search message from ScanActivity -> searchMessage
    private fun message(){
        if (intent.hasExtra("searchMessage")){
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    // Check user
    // If admin fab_add = visible
    private fun checkUser(){
        if (preference.getBoolean("admin")){
            binding.fabAdd.visibility = View.VISIBLE
        }
    }

    // Twice Back pressed to close App
    override fun onBackPressed() {
        backToast = Toast.makeText(this, "Tekan kembali untuk keluar !", Toast.LENGTH_LONG)
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            this.finishAffinity()
            exitProcess(0)
        }else{
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}