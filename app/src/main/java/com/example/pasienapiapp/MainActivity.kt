package com.example.pasienapiapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pasienapiapp.model.LoginRequest
import com.example.pasienapiapp.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email wajib diisi"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Password wajib diisi"
            etPassword.requestFocus()
            return
        }

        lifecycleScope.launch {
            showLoading(true)

            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.apiService.login(request)

                if (response.isSuccessful) {
                    val loginData = response.body()?.data
                    val token = loginData?.token.orEmpty()
                    val userName = loginData?.user?.name.orEmpty()

                    if (token.isNotEmpty() && userName.isNotEmpty()) {
                        val prefs = getSharedPreferences("auth", MODE_PRIVATE)

                        prefs.edit()
                            .putString("token", token)
                            .putString("name", userName)
                            .apply()

                        Toast.makeText(
                            this@MainActivity,
                            "Login berhasil",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@MainActivity, PatientActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showMessage("Token atau data user tidak ditemukan")
                    }
                } else {
                    showMessage("Login gagal. Periksa email dan password.")
                }
            } catch (e: Exception) {
                showMessage("Error: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}