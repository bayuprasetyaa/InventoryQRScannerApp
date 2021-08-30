package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import android.os.Bundle
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListener()
    }

    private fun setupBinding() {
        setContentView( binding.root )
    }

    private fun setupListener(){
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }
    }

}