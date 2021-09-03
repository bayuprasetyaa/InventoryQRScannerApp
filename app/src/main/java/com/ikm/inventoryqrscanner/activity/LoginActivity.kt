package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R
import com.ikm.inventoryqrscanner.databinding.ActivityLoginBinding
import com.ikm.inventoryqrscanner.preferences.PreferenceManager

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val db by lazy { Firebase.firestore }
    private val prefererence by lazy { PreferenceManager(this) }
    private var visibles: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.login.setOnClickListener {
            checkEmpty()
        }

        binding.passwordBtn.setOnClickListener {
            visibles = if (!visibles){
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance())
                true
            }else{
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                false
            }
        }
    }

    private fun checkEmpty(){
        if (binding.username.text.isNotEmpty() && binding.password.text.isNotEmpty()){
            checkData()
        }else{
            Toast.makeText(this, "Kolom Username dan Password tidak boleh kosong !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkData() {
        db.collection("admin")
            .whereEqualTo("username", binding.username.text.toString())
            .whereEqualTo("password", binding.password.text.toString())
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){
                    Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                }else{
                    prefererence.put("admin", true)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
    }
}