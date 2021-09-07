package com.ikm.inventoryqrscanner.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikm.inventoryqrscanner.BaseActivity
import com.ikm.inventoryqrscanner.databinding.ActivityLoginBinding
import com.ikm.inventoryqrscanner.preferences.PreferenceManager

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val bindingFrame by lazy { binding.layoutFrame }
    private val db by lazy { Firebase.firestore }
    private val preference by lazy { PreferenceManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener() // Fungsi Listener
    }

    private fun setupListener() {

        // Remove error when text inputed
        bindingFrame.username.doAfterTextChanged {
            bindingFrame.userInfo.error = null
        }
        bindingFrame.password.doAfterTextChanged {
            bindingFrame.passwordInfo.error = null
        }

        // button login function
        binding.login.setOnClickListener {
            checkEmpty() // Check if username & password column is "NOT EMPTY"
        }
    }



    //-----------------External function--------------------------//

    // Check if username & password column is "NOT EMPTY"
    private fun checkEmpty(){
        if (bindingFrame.username.text.isNullOrEmpty() && bindingFrame.password.text.isNullOrEmpty()){
            bindingFrame.userInfo.error = "Kolom tidak boleh kosong !"
            bindingFrame.passwordInfo.error = "Kolom tidak boleh kosong !"
        }else if (bindingFrame.password.text.isNullOrEmpty()){
            bindingFrame.passwordInfo.error = "Kolom tidak boleh kosong !"
        } else if (bindingFrame.username.text.isNullOrEmpty()){
            bindingFrame.userInfo.error = "Kolom tidak boleh kosong !"
        }else checkUser()
    }

    // Check user
    private fun checkUser() {

        // Query & filtering on database
        db.collection("admin")
            .whereEqualTo("username", bindingFrame.username.text.toString())
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty){

                    // If user is no exist in database
//                    Toast.makeText(this, "Pengguna tidak ditemukan !", Toast.LENGTH_SHORT).show()
                    bindingFrame.password.text = null
                    bindingFrame.userInfo.error = "Pengguna tidak ditemukan"
                    bindingFrame.passwordInfo.error = "  "
                }else
                    // If Success, check the password
                    checkPassword(result)

            }
    }

    // Check Password
    private fun checkPassword(result: QuerySnapshot){
        for (document in result){

            // If Success -> HomeActivity(Preference_admin=True)
            if (document.data["password"] == bindingFrame.password.text.toString()){
                preference.put("admin", true)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()

            // if fail -> make toast
            }else{
                bindingFrame.password.text = null
//                Toast.makeText(this, "Kata sandi tidak cocok !", Toast.LENGTH_SHORT).show()
                bindingFrame.passwordInfo.error = "Kata sandi tidak cocok !"
            }
        }
    }
}