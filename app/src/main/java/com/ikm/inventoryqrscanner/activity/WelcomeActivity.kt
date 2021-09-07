package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import android.os.Bundle
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityWelcomeBinding
import com.ikm.inventoryqrscanner.preferences.PreferenceManager

class WelcomeActivity : BaseActivity() {

    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    private val prefererence by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener() //Fungsi button
    }

    private fun setupListener() {

        // Button "mahasiswa" -> HomeActivity (preference_admin:False)
        binding.mahasiswa.setOnClickListener {
            prefererence.put("admin", false)
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Button "admin" -> LoginActivity
        binding.dosen.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}