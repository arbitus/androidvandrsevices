package com.example.vandrservices

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vandrservices.databinding.ActivityLotFormBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LotFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLotFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLotFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona una fecha")
            .setSelection(today)
            .build()

        binding.etDate.setOnClickListener {
            datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }
        val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Date(today))
        binding.etDate.setText(formattedDate)
        datePicker.addOnPositiveButtonClickListener { selection ->
            val date = Date(selection)
            val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            binding.etDate.setText(formatted)
        }
        val calendar = Calendar.getInstance()
        val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
        binding.editTextArvWeek.setText(weekOfYear.toString())

        val opciones = listOf("Mango", "Banana", "Avocado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opciones)

        binding.dropdown.setAdapter(adapter)

        binding.dropdown.setOnClickListener {
            binding.dropdown.showDropDown()
        }
    }
}