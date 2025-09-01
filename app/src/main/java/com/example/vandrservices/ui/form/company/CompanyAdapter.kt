package com.example.vandrservices.ui.form.company

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vandrservices.R
import com.example.vandrservices.data.CompanyResponse
import com.example.vandrservices.data.CompanyUI

class CompanyAdapter(
    private var companyList: List<CompanyUI> = emptyList(),
    private val onItemSelected:(Int) -> Unit
): RecyclerView.Adapter<CompanyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return CompanyViewHolder(inflate.inflate(R.layout.item_company, parent, false))
    }

    override fun getItemCount(): Int = companyList.size

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(companyList[position], onItemSelected)
    }

    fun updateList(companyList: List<CompanyUI>) {
        this.companyList = companyList
        notifyDataSetChanged()
    }
}
