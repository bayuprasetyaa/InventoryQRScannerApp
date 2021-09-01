package com.ikm.inventoryqrscanner.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.R

class ListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }
}