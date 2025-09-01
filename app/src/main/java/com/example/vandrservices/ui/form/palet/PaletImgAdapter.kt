package com.example.vandrservices.ui.form.palet

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.vandrservices.R

class PaletImageAdapter(
    private val tiposDisponibles: List<String>
) : RecyclerView.Adapter<PaletImageAdapter.ViewHolder>() {

    data class PaletImageItem(
        val uri: Uri,
        var tipo: String? = null
    )

    private val items = mutableListOf<PaletImageItem>()

    fun addImage(uri: Uri) {
        items.add(PaletImageItem(uri))
        notifyItemInserted(items.size - 1)
    }

    fun getItems() : List<PaletImageItem> =  items

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

        fun bind(item: PaletImageItem) {
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