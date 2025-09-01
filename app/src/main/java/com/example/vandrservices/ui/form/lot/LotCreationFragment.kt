package com.example.vandrservices.ui.form.lot

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.vandrservices.R
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.databinding.FragmentLotCreationBinding
import com.example.vandrservices.domain.model.Lot
import com.example.vandrservices.domain.model.LotToJson
import com.example.vandrservices.ui.form.company.CompanyAdapter
import com.example.vandrservices.ui.form.isInternetAvailable
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class LotCreationFragment : Fragment() {

    private val lotCreateViewModel by viewModels<LotCreationViewModel>()
    private var _binding: FragmentLotCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var retrofit: Retrofit
    private val apiService by lazy { retrofit.create(ApiService::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLotCreationBinding.inflate(layoutInflater, container, false)
        retrofit = RetrofitControles.getRetrofit()
        initUI()
        return binding.root

    }

    private fun initUI() {
        initUIState()
        val companyId = arguments?.getInt("id")
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("pick a date")
            .setSelection(today)
            .build()

        binding.etDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        }
        val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        formattedDate.timeZone = TimeZone.getDefault()
        val todayUtc = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = todayUtc
        binding.etDate.setText(formattedDate.format(calendar.time))

        datePicker.addOnPositiveButtonClickListener { selection ->
            // selection = fecha en UTC
            val selectedCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            selectedCalendar.timeInMillis = selection

            // Ahora la conviertes a local
            val localCalendar = Calendar.getInstance()
            localCalendar.timeInMillis = selectedCalendar.timeInMillis

            binding.etDate.setText(formattedDate.format(localCalendar.time))}
        val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
        binding.editTextArvWeek.setText(weekOfYear.toString())
        binding.btnSubmit.setOnClickListener {
            val lotNumber = binding.etLotNumber.editText?.text.toString()
            if (lotNumber.isEmpty()){
                binding.etLotNumber.error = "Este campo es obligatorio"
                return@setOnClickListener
            }
            val variety = binding.dropdown.text.toString()
            if (variety.isEmpty()){
                binding.dropdown.error = "Este campo es obligatorio"
                return@setOnClickListener
            }
            val newData = Lot(
                localId = UUID.randomUUID().toString(),
                company = companyId.toString(),
                lotNumber = binding.etLotNumber.editText?.text.toString(),
                arrivalPort = binding.etLotArrivalPort.editText?.text.toString(),
                insPlace = binding.etLotInsPlace.editText?.text.toString(),
                insDate = binding.etLotInsDate.editText?.text.toString(),
                exporter = binding.etLotExporter.editText?.text.toString(),
                invoice = binding.etLotInvoice.editText?.text.toString(),
                arvWeek = binding.etLotArvWeek.editText?.text.toString(),
                origin = binding.etLotOrigin.editText?.text.toString(),
                auditor = binding.etLotAuditor.editText?.text.toString(),
                cases = binding.etLotCases.editText?.text.toString(),
                grower = binding.etLotGrower.editText?.text.toString(),
                label = binding.etLotLabel.editText?.text.toString(),
                variety = binding.dropdown.text.toString()
            )
            var apiDate = binding.etLotInsDate.editText?.text.toString()
            val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(apiDate)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            apiDate = outputFormat.format(parsedDate!!)

            val data = LotToJson(
                id = null,
                arrivalPort = binding.etLotArrivalPort.editText?.text.toString(),
                arrivalWeek = binding.etLotArvWeek.editText?.text.toString().toIntOrNull(),
                auditor = binding.etLotAuditor.editText?.text.toString(),
                cases = binding.etLotCases.editText?.text.toString().toIntOrNull(),
                company = companyId ?: 0,
                exporter = binding.etLotExporter.editText?.text.toString(),
                grower = binding.etLotGrower.editText?.text.toString(),
                insDate = apiDate,
                insPlace = binding.etLotInsPlace.editText?.text.toString(),
                invoice = binding.etLotInvoice.editText?.text.toString(),
                label = binding.etLotLabel.editText?.text.toString(),
                lotNumber = binding.etLotNumber.editText?.text.toString(),
                origin = binding.etLotOrigin.editText?.text.toString(),
                variedad = binding.dropdown.text.toString()
            )

            var lotId: Int = 0
            if (isInternetAvailable(requireContext())) {
                lifecycleScope.launch {
                    val response =
                        apiService.PostToken(username = "fabian", password = "Zarathustra40")
                    val tokenResponse = response.body()
                    if (response.isSuccessful && tokenResponse != null) {
                        val responseLot =
                            apiService.createLot("Bearer ${tokenResponse.access}", data)
                        if (responseLot.isSuccessful) {
                            Log.i(
                                "LotCreateFragment",
                                "Lote enviado correctamente al servidor. id: ${responseLot.body()?.id}"
                            )
                            lotId = responseLot.body()?.id ?: 0
                            Log.i("LotCreateFragment", "Lot id: ${lotId}")
                            val bundle = bundleOf(
                                "variety" to binding.dropdown.text.toString(),
                                "lotId" to lotId,
                                "grower" to binding.etLotGrower.editText?.text.toString(),
                                "packDate" to binding.etLotInsDate.editText?.text.toString(),
                                "cases" to binding.etLotCases.editText?.text.toString(),
                                "label" to binding.etLotLabel.editText?.text.toString(),
                            )
                            Log.i("LotCreateFragment", "Lote bundle id: ${bundle.toString()}")
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Lote guardado")
                                .setMessage("El lote se ha guardado con éxito.")
                                .setPositiveButton("Aceptar") { dialog, _ ->
                                    dialog.dismiss()
                                    findNavController().navigate(R.id.paletCreationFragment, bundle)
                                }.show()
                        } else {
                            lotCreateViewModel.addLot(newData)
                            Log.e(
                                "LotCreateFragment",
                                "Error al enviar lote: ${responseLot.code()}"
                            )
                        }
                    }else{
                        lotCreateViewModel.addLot(newData)
                        Log.e(
                            "LotCreateFragment",
                            "Error al autenticar: ${response.code()}"
                        )
                    }
                }
            }else{
                lotCreateViewModel.addLot(newData)
                val bundle = bundleOf(
                    "variety" to binding.dropdown.text.toString(),
                    "lotId" to lotId,
                    "grower" to binding.etLotGrower.editText?.text.toString(),
                    "packDate" to binding.etLotInsDate.editText?.text.toString(),
                    "cases" to binding.etLotCases.editText?.text.toString(),
                    "label" to binding.etLotLabel.editText?.text.toString(),
                )
                Log.i("LotCreateFragment", "Lote bundle id: ${bundle.toString()}")
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Lote guardado")
                    .setMessage("El lote se ha guardado con éxito en la app.")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                        findNavController().navigate(R.id.paletCreationFragment, bundle)
                    }.show()
                CoroutineScope(Dispatchers.IO).launch {
                    lotCreateViewModel.lots.collect { lots ->
                        Log.i("LotCreateFragment", "Lotes: ${lots.toString()}")
                    }
                }
            }
        }
    }
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                lotCreateViewModel.varietys.collect{ varietys ->
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        varietys
                    )
                    binding.dropdown.setAdapter(adapter)

                    binding.dropdown.setOnClickListener {
                        binding.dropdown.showDropDown()
                    }
                }
            }
        }
    }
}