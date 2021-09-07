package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R
import com.ikm.inventoryqrscanner.databinding.ActivityLoginBinding
import com.ikm.inventoryqrscanner.preferences.PreferenceManager
import org.w3c.dom.Text

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val bindingFrame by lazy { binding.loginFrame }
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
    }

    private fun checkEmpty(){

    }

    private fun checkData() {

            
    }
}