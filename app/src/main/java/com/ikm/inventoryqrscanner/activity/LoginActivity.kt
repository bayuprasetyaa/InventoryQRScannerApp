package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R
import com.ikm.inventoryqrscanner.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.login.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}