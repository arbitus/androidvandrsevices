package com.example.vandrservices.ui.form.lot

import com.example.vandrservices.R
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView



class LotImageAdapter(
    private val tiposDisponibles: List<String>
) : RecyclerView.Adapter<LotImageAdapter.ViewHolder>() {

    data class LotImageItem(
        val uri: Uri,
        var tipo: String? = null
    )

    private val items = mutableListOf<LotImageItem>()

    fun addImage(uri: Uri) {
        items.add(LotImageItem(uri))
        notifyItemInserted(items.size - 1)
    }

    fun getItems() : List<LotImageItem> =  items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_type, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgPreview: ImageView = view.findViewById(R.id.imgPreview)
        private val dropdownTipo: AutoCompleteTextView = view.findViewById(R.id.dropdownTipo)

        fun bind(item: LotImageItem) {
            imgPreview.setImageURI(item.uri)
            val adapter = ArrayAdapter(itemView.context,
                android.R.layout.simple_dropdown_item_1line, tiposDisponibles)
            dropdownTipo.setAdapter(adapter)
            dropdownTipo.setText(item.tipo ?: "", false)

            dropdownTipo.setOnClickListener {
                dropdownTipo.showDropDown()
            }

            dropdownTipo.setOnItemClickListener { _, _, pos, _ ->
                item.tipo = tiposDisponibles[pos]
            }
            dropdownTipo.addTextChangedListener {
                item.tipo = it.toString()
            }
        }
    }
}