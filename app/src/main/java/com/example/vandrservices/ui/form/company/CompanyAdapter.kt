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
    fun loadOfflineCompanies() {
        val offlineList = listOf(
            CompanyUI(133, "San Jorge", imageRes = R.drawable.san_jorge),
            CompanyUI(166, "Pure Nature Produce", imageRes = R.drawable.pure_nature),
            CompanyUI(199, "Dayka Hackett LLC", imageRes = R.drawable.daykahackettfrutura_color),
            CompanyUI(232, "Frutura Texas", imageRes = R.drawable.frutura_logo),
            CompanyUI(4, "Ground Fresh International", imageRes = R.drawable.ground_fresh),
            CompanyUI(3, "Coliman", imageRes = R.drawable.coliman),
            CompanyUI(1, "Level Berries", imageRes = R.drawable.level_berries),
            CompanyUI(2, "BST", imageRes = R.drawable.bst),
            CompanyUI(265, "Produce Nation", imageRes = R.drawable.produce_notion),
            CompanyUI(70, "Chanitos", imageRes = R.drawable.chanitos),
            CompanyUI(34, "Ganfer Fresh", imageRes = R.drawable.ganfer),
            CompanyUI(100, "Farmacopia Farms", imageRes = R.drawable.farmacopia),
        )
        updateList(offlineList)
    }
}
