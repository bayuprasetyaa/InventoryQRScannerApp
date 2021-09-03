package com.ikm.inventoryqrscanner.activity

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityScanBinding

class ScanActivity : BaseActivity() {

    private val binding by lazy { ActivityScanBinding.inflate(layoutInflater) }
    private lateinit var codeScanner : CodeScanner
    private lateinit var number : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupPermissions()
        codeScanner()
    }

    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(this, CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), CAMERA_REQ)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }

    private fun codeScanner(){

        codeScanner = CodeScanner(this,binding.scan)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback{
                runOnUiThread {
                    number = it.text.toString()
                    startActivity(
                        Intent(this@ScanActivity, ProductActivity::class.java)
                            .putExtra("number", number))
                    finish()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            binding.scan.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        super.onBackPressed()
    }

}