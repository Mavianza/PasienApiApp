package com.example.pasienapiapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasienapiapp.adapter.PasienAdapter
import com.example.pasienapiapp.network.RetrofitClient
import kotlinx.coroutines.launch

class PatientActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvPasien: RecyclerView
    private lateinit var adapter: PasienAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)

        tvUserName = findViewById(R.id.tvUserName)
        progressBar = findViewById(R.id.progressBarPasien)
        rvPasien = findViewById(R.id.rvPasien)

        adapter = PasienAdapter()
        rvPasien.layoutManager = LinearLayoutManager(this)
        rvPasien.adapter = adapter

        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        val token = prefs.getString("token", "") ?: ""
        val name = prefs.getString("name", "") ?: ""

        tvUserName.text = "Login sebagai: $name"

        if (token.isEmpty()) {
            showMessage("Token tidak ditemukan. Silakan login ulang.")
            finish()
            return
        }

        loadPasien(token)
    }

    private fun loadPasien(token: String) {
        lifecycleScope.launch {
            showLoading(true)

            try {
                val bearerToken = "Bearer $token"
                val response = RetrofitClient.apiService.getPasien(bearerToken)

                if (response.isSuccessful) {
                    val pasienList = response.body()?.data ?: emptyList()
                    adapter.setData(pasienList)

                    if (pasienList.isEmpty()) {
                        showMessage("Data pasien kosong")
                    }
                } else {
                    showMessage("Gagal mengambil data pasien: ${response.code()}")
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
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}