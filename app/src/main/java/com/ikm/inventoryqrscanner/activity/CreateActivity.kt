package com.ikm.inventoryqrscanner.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R
import com.ikm.inventoryqrscanner.databinding.ActivityCreateBinding
import com.ikm.inventoryqrscanner.fragment.DateFragment
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.util.stringToTimestamp

class CreateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private val db by lazy { Firebase.firestore }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupListener() // Fungsi Button (Listener)

    }

    private fun setupView(){
        binding.btnDelete.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.error2.visibility = View.GONE
        binding.errorId.visibility = View.GONE
        binding.errorProduct.visibility = View.GONE
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

            //Check the field
            if (binding.editId.text.isNullOrEmpty() && binding.editProduct.text.isNullOrEmpty()){
                binding.error.visibility = View.VISIBLE
                binding.error2.visibility = View.VISIBLE
                binding.errorId.visibility = View.VISIBLE
                binding.errorProduct.visibility = View.VISIBLE

                binding.editId.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_red)
                binding.editProduct.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_red)

            }else if (binding.editId.text.isNullOrEmpty()){
                binding.error.visibility = View.VISIBLE
                binding.errorId.visibility = View.VISIBLE
            }else if (binding.editProduct.text.isNullOrEmpty()){
                binding.error2.visibility = View.VISIBLE
                binding.errorProduct.visibility = View.VISIBLE
            }else{
                // Get data from EditText field
                val product = Product(
                    number = binding.editId.text.toString(),
                    product = binding.editProduct.text.toString(),
                    expDate = stringToTimestamp(binding.editDate.text.toString()),
                    amount = binding.editAmount.text.toString(),
                    type = binding.editType.selectedItem.toString(),
                    location = binding.editLocation.text.toString(),
                    condition = binding.editCondition.selectedItem.toString(),
                    description = binding.editDesc.text.toString())

                checkData(product) // Check Id product if exist in database
            }
        }

        binding.editId.doOnTextChanged { text, start, before, count ->
            binding.error.visibility = View.GONE
            binding.errorId.visibility = View.GONE
            binding.editId.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)
        }

        binding.editProduct.doOnTextChanged { text, start, before, count ->
            binding.error2.visibility = View.GONE
            binding.errorProduct.visibility = View.GONE
            binding.editProduct.backgroundTintList = ContextCompat.getColorStateList(this, R.color.black)
        }
    }


    //--------------------- External FUnction ----------------//

    // send data function
    private fun sendData(product: Product) {

        // Create data in firestore database
        db.collection("item_description")
            .document(product.number!!)
            .set(product)
            .addOnSuccessListener {
                Log.e(TAG, "DocumentSnapshot added with ID: ${product.number}")
                Toast.makeText(applicationContext, "Produk Berhasil Ditambahkan !", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error", e)
            }
    }

    // check data
    private fun checkData(product: Product){
        db.collection("item_description")
            .whereEqualTo("number", product.number)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){
                    // If data is no exist in database
                    checkDuplicate(product) // Check duplicate of product name in database
                }else{
                    // If Success, make toast
                    binding.errorId.text = getString(R.string.errorID)
                    binding.errorId.visibility = View.VISIBLE
                }
            }
    }

    // check duplicate
    private fun checkDuplicate(product: Product){
        db.collection("item_description")
            .whereEqualTo("product", product.product)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){
                    // If data is no exist in database
                    sendData(product)
                }else{
                    // If Success, make toast
                    AlertDialog.Builder(this@CreateActivity).apply {
                        setTitle("Produk")
                        setMessage("Produk ${product.product} sudah ada, tetap lanjutkan ?")
                        setNegativeButton("Batal"){dialog,_ -> dialog.dismiss()}
                        setPositiveButton("Lanjutkan"){dialog,_ ->
                            sendData(product)
                            dialog.dismiss()}
                    }.show()
                }
            }
    }
}