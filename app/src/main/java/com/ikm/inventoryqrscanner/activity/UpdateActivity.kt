package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R
import com.ikm.inventoryqrscanner.databinding.ActivityCreateBinding
import com.ikm.inventoryqrscanner.fragment.DateFragment
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.util.*
import java.util.*

class UpdateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private val db by lazy { Firebase.firestore }
    private val number by lazy { intent.getStringExtra("number") }
    private lateinit var items: Product
    private lateinit var oldProduct: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupListener() // Fungsi Button (Listener)
    }

    override fun onStart() {
        super.onStart()
        detailProduct()
    }

    private fun setupView(){
        binding.container.requestFocus()

        binding.btnDelete.visibility = View.VISIBLE
        binding.btnSave.text = getString(R.string.update)

        binding.editId.isClickable = false
        binding.editId.isFocusable = false
        binding.editId.isEnabled = false
        binding.editId.setTextColor(ContextCompat.getColorStateList(this, R.color.gray))

        binding.error.visibility = View.GONE
        binding.error2.visibility = View.GONE
        binding.error3.visibility = View.GONE

        binding.errorId.visibility = View.GONE
        binding.errorProduct.visibility = View.GONE
        binding.errorDate.visibility = View.GONE
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
            checkDateFormat()
        }

        binding.btnDelete.setOnClickListener {
            deleteItem(number!!)
        }

        binding.editId.doOnTextChanged { _, _, _, _ ->
            binding.error.visibility = View.GONE
            binding.errorId.visibility = View.GONE
            binding.editId.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
        }

        binding.editProduct.doOnTextChanged { _, _, _, _ ->
            binding.error2.visibility = View.GONE
            binding.errorProduct.visibility = View.GONE
            binding.editProduct.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
        }

        binding.editDate.doOnTextChanged { _, _, _, _ ->
            binding.error3.visibility = View.GONE
            binding.errorDate.visibility = View.GONE
            binding.editDate.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
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
                Toast.makeText(applicationContext, "Produk Berhasil Diupdate !", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error", e)
            }
    }


    // check duplicate
    private fun checkDuplicate(){

        db.collection("item_description")
            .whereEqualTo("product", items.product)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){
                    checkEmptyField()
                }else{
                    for (document in result){
                        if (document.data["product"] == oldProduct){
                            // If data is no exist in database or same with field product
                            Log.e(TAG, "input doc number : ${document["product"]} input $oldProduct")
                            checkEmptyField()
                        }else {
                            AlertDialog.Builder(this@UpdateActivity).apply {
                                setTitle("Produk")
                                setMessage("Produk ${document.data["product"]} sudah ada, tetap lanjutkan ?")
                                setNegativeButton("Batal"){dialog,_ -> dialog.dismiss()}
                                setPositiveButton("Lanjutkan"){dialog,_ ->
                                    checkEmptyField()
                                    dialog.dismiss()}
                            }.show()
                        }
                    }
                }
            }
    }

    //check empty field
    private fun checkEmptyField(){
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
            sendData(items)
        }
    }

    private fun checkDateFormat(){

        Log.e(TAG, "Text : ${binding.editDate.text!!.length}")
        if (binding.editDate.text.isNullOrBlank()){
            getDataFromField()
            checkDuplicate()
        }else if (binding.editDate.text!!.length in 1..9){
            Log.e(TAG, "Text :Berhasil !!!! ${binding.editDate.text!!.length}")
            binding.error3.visibility = View.VISIBLE
            binding.errorDate.visibility = View.VISIBLE
            binding.editDate.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_red)
        } else if (!dateFormat(binding.editDate.text.toString())){
            binding.error3.visibility = View.VISIBLE
            binding.errorDate.visibility = View.VISIBLE
            binding.editDate.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_red)
        } else {
            getDataFromField()
            checkDuplicate()
        }
    }

    private fun detailProduct() {

        db.collection("item_description")
            .document(number!!)
            .get()
            .addOnSuccessListener { document ->
                items = Product(
                    number = document["number"].toString(),
                    product = document["product"].toString(),
                    expDate = document["expDate"] as? Timestamp,
                    amount = document["amount"].toString(),
                    type = document["type"].toString(),
                    location = document["location"].toString(),
                    condition = document["condition"].toString(),
                    description = document["description"].toString(),
                    created = document["created"] as Timestamp,
                    count = document["count"].toString().toInt())

                oldProduct = items.product.toString()

                binding.editId.setText(items.number.toString())
                binding.editProduct.setText(items.product)
                binding.editDate.setText(timestampToString(items.expDate))
                binding.editAmount.setText(items.amount.toString())
                binding.editType.setSelection(spinnerType(items.type.toString()), true)
                binding.editLocation.setText(items.location)
                binding.editCondition.setSelection(spinnerCondition(items.type.toString()), true)
                binding.editDesc.setText(items.description)
            }

            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents.", exception) }
    }

    private fun deleteItem(number: String){
        db.collection("item_description").document(number)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "${items.product} Dihapus!!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            .addOnFailureListener {  }
    }

    private fun getDataFromField(){
        // Get data from EditText field
        items.number = binding.editId.text.toString()
        items.product = binding.editProduct.text.toString()
        items.expDate = stringToTimestamp(binding.editDate.text.toString())
        items.amount = binding.editAmount.text.toString()
        items.type = binding.editType.selectedItem.toString()
        items.location = binding.editLocation.text.toString()
        items.condition = binding.editCondition.selectedItem.toString()
        items.description = binding.editDesc.text.toString()
    }
}