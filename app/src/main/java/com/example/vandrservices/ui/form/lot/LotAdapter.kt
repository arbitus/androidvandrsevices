package com.example.vandrservices.ui.form.lot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vandrservices.databinding.ItemLotBinding
import com.example.vandrservices.domain.model.Lot

class LotAdapter(private var items: List<Lot>) :
    RecyclerView.Adapter<LotAdapter.LotViewHolder>() {

    inner class LotViewHolder(private val binding: ItemLotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lot: Lot) {
            binding.tvLotName.text = lot.lotNumber
            binding.tvLotId.text = lot.localId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotViewHolder {
        val binding = ItemLotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LotViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Lot>) {
        items = newItems
        notifyDataSetChanged()
    }
}