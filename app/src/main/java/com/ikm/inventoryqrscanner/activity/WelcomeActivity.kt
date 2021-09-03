package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R
import com.ikm.inventoryqrscanner.databinding.ActivityWelcomeBinding
import com.ikm.inventoryqrscanner.preferences.PreferenceManager

class WelcomeActivity : BaseActivity() {

    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    private val prefererence by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {
        binding.mahasiswa.setOnClickListener {
            prefererence.put("admin", false)
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.dosen.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}