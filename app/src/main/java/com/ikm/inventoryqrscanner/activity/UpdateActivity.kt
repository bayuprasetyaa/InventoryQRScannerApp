package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityCreateBinding
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.util.spinnerCondition
import com.ikm.inventoryqrscanner.util.spinnerType
import com.ikm.inventoryqrscanner.util.timestampToString
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }
    val number by lazy { intent.getStringExtra("number") }
    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private lateinit var items : Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupListener() // fungsi button
    }

    override fun onStart() {
        super.onStart()
        detailProduct()
    }


    private fun setupView() {
        binding.btnSave.setText("SIMPAN PERUBAHAN")
        binding.btnDelete.isVisible = true
    }

    private fun setupListener() {

        binding.btnSave.setOnClickListener{

            // Update data dari TextView ke database firesore
            val date = binding.editDate.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            items.number = binding.editId.text.toString()
            items.product = binding.editProduct.text.toString()
            items.expDate = Timestamp(dateFormat.parse(date))
            items.amount = binding.editAmount.text.toString().toInt()
            items.type = binding.editType.selectedItem.toString()
            items.location = binding.editLocation.text.toString()
            items.condition = binding.editCondition.selectedItem.toString()
            items.description = binding.editDesc.text.toString()

            db.collection("item_description").document(number!!)
                .set(items)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    Toast.makeText(applicationContext, "Berhasil Diperbarui!!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        binding.btnDelete.setOnClickListener {

            //Delete database berdasarkan dari id Number

            AlertDialog.Builder(this@UpdateActivity).apply {
                setTitle("Hapus")
                setMessage("Hapus ${items.product} dari list ?")
                setNegativeButton("Batal"){dialog,_ -> dialog.dismiss()}
                setPositiveButton("Hapus"){dialog,_ ->
                    deleteItem(items.number!!)
                    dialog.dismiss()}
            }.show()
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
                    expDate = document["expDate"] as Timestamp,
                    amount = document["amount"].toString().toInt(),
                    type = document["type"].toString(),
                    location = document["location"].toString(),
                    condition = document["condition"].toString(),
                    description = document["description"].toString())

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

    private fun deleteItem(id: String){
        db.collection("item_description").document(number!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "${items.product} Dihapus!!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            .addOnFailureListener {  }
    }
}