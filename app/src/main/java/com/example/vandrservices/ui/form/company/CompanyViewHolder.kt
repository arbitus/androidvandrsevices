package com.example.vandrservices.ui.form.company

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vandrservices.R
import com.example.vandrservices.data.CompanyResponse
import com.example.vandrservices.data.CompanyUI
import com.example.vandrservices.databinding.ItemCompanyBinding
import com.squareup.picasso.Picasso

class CompanyViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val ivImage: ImageView = view.findViewById(R.id.ivImageCompany)
    private val tvName: TextView = view.findViewById(R.id.tvCompanyName)

    fun bind(company: CompanyUI, onItemSelected: (Int) -> Unit) {
        tvName.text = company.name

        if (company.imageRes != null) {
            // Imagen local
            ivImage.setImageResource(company.imageRes)
        } else if (!company.imageUrl.isNullOrEmpty()) {
            // Imagen desde internet
            Glide.with(itemView.context)
                .load(company.imageUrl)
                .into(ivImage)
        } else {
            ivImage.setImageResource(R.drawable.ic_placeholder_background) // fallback
        }

        itemView.setOnClickListener { onItemSelected(company.id) }
    }
}
