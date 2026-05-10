package com.example.pasienapiapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pasienapiapp.R
import com.example.pasienapiapp.model.Pasien

class PasienAdapter : RecyclerView.Adapter<PasienAdapter.PasienViewHolder>() {

    private val pasienList = mutableListOf<Pasien>()

    fun setData(newData: List<Pasien>) {
        pasienList.clear()
        pasienList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasienViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pasien, parent, false)
        return PasienViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasienViewHolder, position: Int) {
        holder.bind(pasienList[position])
    }

    override fun getItemCount(): Int {
        return pasienList.size
    }

    class PasienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        private val tvTanggalLahir: TextView = itemView.findViewById(R.id.tvTanggalLahir)
        private val tvJenisKelamin: TextView = itemView.findViewById(R.id.tvJenisKelamin)
        private val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        private val tvTelepon: TextView = itemView.findViewById(R.id.tvTelepon)

        fun bind(pasien: Pasien) {
            tvNama.text = pasien.nama
            tvTanggalLahir.text = pasien.tanggal_lahir
            tvJenisKelamin.text = formatJenisKelamin(pasien.jenis_kelamin)
            tvAlamat.text = pasien.alamat
            tvTelepon.text = pasien.no_telepon
        }

        private fun formatJenisKelamin(jenisKelamin: String): String {
            return when (jenisKelamin.uppercase()) {
                "L" -> "Laki-laki"
                "P" -> "Perempuan"
                else -> jenisKelamin
            }
        }
    }
}