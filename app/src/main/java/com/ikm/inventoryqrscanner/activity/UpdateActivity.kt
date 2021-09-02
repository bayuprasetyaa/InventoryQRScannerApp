package com.ikm.inventoryqrscanner.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityCreateBinding
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.util.timestampToString
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : BaseActivity() {

    private val db by lazy { Firebase.firestore }
    val id by lazy { intent.getStringExtra("id") }
    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }
    private lateinit var items : Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupListener()
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

            val date = binding.editDate.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            items.number = binding.editId.text.toString().toInt()
            items.product = binding.editProduct.text.toString()
            items.expDate = Timestamp(dateFormat.parse(date))
            items.amount = binding.editAmount.text.toString().toInt()
            items.type = binding.editType.text.toString()
            items.location = binding.editLocation.text.toString()
            items.condition = binding.editCondition.text.toString()
            items.description = binding.editDesc.text.toString()

            db.collection("item_description").document(id!!)
                .set(items)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    Toast.makeText(applicationContext, "Berhasil Diperbarui!!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this@UpdateActivity).apply {
                setTitle("Hapus")
                setMessage("Hapus ${items.product} dari list ?")
                setNegativeButton("Batal"){dialog,_ -> dialog.dismiss()}
                setPositiveButton("Hapus"){dialog,_ ->
                    deleteItem(items.id!!)
                    dialog.dismiss()}
            }.show()
        }


    }

    private fun detailProduct() {
        db.collection("item_description")
            .document(id!!)
            .get()
            .addOnSuccessListener { document ->

                items = Product(
                    id = document.reference.id,
                    number = document["number"].toString().toInt(),
                    product = document["product"].toString(),
                    expDate = document["expDate"] as Timestamp,
                    amount = document["amount"].toString().toInt(),
                    type = document["type"].toString(),
                    location = document["location"].toString(),
                    condition = document["condition"].toString(),
                    description = document["description"].toString()
                )

                binding.editId.setText(items.number.toString())
                binding.editProduct.setText(items.product)
                binding.editDate.setText(timestampToString(items.expDate))
                binding.editAmount.setText(items.amount.toString())
                binding.editType.setText(items.type)
                binding.editLocation.setText(items.location)
                binding.editCondition.setText(items.condition)
                binding.editDesc.setText(items.description)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents.", exception) }
    }

    private fun deleteItem(id: String){
        db.collection("item_description").document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "${items.product} Dihapus!!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {  }
    }

}