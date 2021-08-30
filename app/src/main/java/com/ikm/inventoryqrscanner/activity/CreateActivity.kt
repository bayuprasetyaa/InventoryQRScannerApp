package com.ikm.inventoryqrscanner.activity

import android.os.Bundle
import android.util.Log
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityCreateBinding
import com.ikm.inventoryqrscanner.fragment.DateFragment

class CreateActivity : BaseActivity() {

    private val binding by lazy { ActivityCreateBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {

        binding.editDate.setOnClickListener {
            DateFragment(object : DateFragment.DateListener {
                override fun onSuccess(date: String) {
                    binding.editDate.setText(date)
                }

            }).apply { show(supportFragmentManager, "dateFragment")}
        }

    }


}