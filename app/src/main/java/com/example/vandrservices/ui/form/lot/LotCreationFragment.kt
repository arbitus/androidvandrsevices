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
import com.example.vandrservices.domain.model.User
import com.example.vandrservices.ui.form.Util.getLotAutofill
import com.example.vandrservices.ui.form.isInternetAvailable
import com.example.vandrservices.ui.login.LoginViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.UUID

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class LotCreationFragment : Fragment() {

    private val lotCreateViewModel by viewModels<LotCreationViewModel>()
    private var _binding: FragmentLotCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var retrofit: Retrofit
    private val apiService by lazy { retrofit.create(ApiService::class.java) }
    private val userViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLotCreationBinding.inflate(layoutInflater, container, false)
        retrofit = RetrofitControles.getRetrofit()
        initUIState()
        setupDatePicker()
        setupAutofillDropdown()
        setupSubmitButton()
        fillAuditor()
        return binding.root
    }

    private fun setupDatePicker() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.pick_a_date))
            .setSelection(today)
            .build()

        binding.etDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        }

        val formattedDate = Utils.getCurrentFormattedDate()
        binding.etDate.setText(formattedDate)

        datePicker.addOnPositiveButtonClickListener { selection ->
            binding.etDate.setText(Utils.formatDate(selection))
        }
        binding.editTextArvWeek.setText(Utils.getCurrentWeekOfYear().toString())
    }


    private fun setupAutofillDropdown() {
        binding.dropdown.setOnItemClickListener { _, _, _, _ ->
            val selectedVariety = binding.dropdown.text.toString()
            val companyId = arguments?.getInt("id")
            val autofill = getLotAutofill(selectedVariety, companyId)
            autofill.exporter?.let { binding.etLotExporter.editText?.setText(it) }
            autofill.inspectionPlace?.let { binding.etLotInsPlace.editText?.setText(it) }
            autofill.label?.let { binding.etLotLabel.editText?.setText(it) }
            autofill.grower?.let { binding.etLotGrower.editText?.setText(it) }
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            val companyId = arguments?.getInt("id")
            companyId?.let{
                lotCreateViewModel.setEmpresaId(it)
            }
            val empresaIdPersisted = lotCreateViewModel.getEmpresaId()
            val lot = buildLot(empresaIdPersisted)
            val lotToJson = buildLotToJson(companyId, binding.etLotInsDate.editText?.text.toString())
            sendLot(lot, lotToJson)
        }
    }

    private fun validateFields(): Boolean {
        var valid = true
        if (binding.etLotNumber.editText?.text.isNullOrEmpty()) {
            binding.etLotNumber.error = getString(R.string.field_required)
            valid = false
        }
        if (binding.dropdown.text.isNullOrEmpty()) {
            binding.dropdown.error = getString(R.string.field_required)
            valid = false
        }
        return valid
    }

    private fun buildLot(companyId: Int?): Lot {
        return Lot(
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
    }

    private fun buildLotToJson(companyId: Int?, date: String): LotToJson {
        val apiDate = Utils.formatDateToApi(date)
        return LotToJson(
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
    }

    private fun sendLot(newData: Lot, data: LotToJson) {
        var lotId = 0
        if (isInternetAvailable(requireContext())) {
            lifecycleScope.launch {
                val user: User? = userViewModel.usersFlow.firstOrNull()?.firstOrNull()
                val token = user?.token
                if (!token.isNullOrEmpty()) {
                    val responseLot = apiService.createLot("Bearer $token", data)
                    if (responseLot.isSuccessful) {
                        lotId = responseLot.body()?.id ?: 0
                        withContext(Dispatchers.Main) {
                            showLotSavedDialog(lotId, localSave = false)
                        }
                    } else {
                        showError(getString(R.string.server_error))
                    }
                }
            }
        } else {
            lotCreateViewModel.addLot(newData)
            showLotSavedDialog(lotId, localSave = true, localLotId = newData.localId)
        }
    }

    private fun showLotSavedDialog(lotId: Int, localSave: Boolean, localLotId: String? = null) {
        val bundle = bundleOf(
            "variety" to binding.dropdown.text.toString(),
            "lotId" to lotId,
            "grower" to binding.etLotGrower.editText?.text.toString(),
            "packDate" to binding.etLotInsDate.editText?.text.toString(),
            "cases" to binding.etLotCases.editText?.text.toString(),
            "label" to binding.etLotLabel.editText?.text.toString()
        )
        if (localSave && localLotId != null) {
            bundle.putString("localLotId", localLotId)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.lot_saved_title))
            .setMessage(
                if (localSave)
                    getString(R.string.lot_saved_local)
                else
                    getString(R.string.lot_saved_success)
            )
            .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
                dialog.dismiss()
                findNavController().navigate(R.id.paletCreationFragment, bundle)
            }.show()
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun fillAuditor() {
        lifecycleScope.launch {
            val user: User? = userViewModel.usersFlow.firstOrNull()?.firstOrNull()
            binding.editTextAuditor.setText(user?.name ?: "")
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                lotCreateViewModel.varietys.collect { varieties ->
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        varieties
                    )
                    binding.dropdown.setAdapter(adapter)
                    binding.dropdown.setOnClickListener { binding.dropdown.showDropDown() }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}