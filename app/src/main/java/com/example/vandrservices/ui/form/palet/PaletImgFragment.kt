package com.example.vandrservices.ui.form.palet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vandrservices.R
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.ui.form.lot.LotImgViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class PaletImagesFragment : Fragment() {

    private lateinit var adapter: PaletImageAdapter
    private val lotImgViewModel by viewModels<LotImgViewModel>()
    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris.forEach { adapter.addImage(it) }
    }

    private val apiService by lazy { RetrofitControles.getRetrofit().create(ApiService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_palet_img, container, false)
        val etLotNumber = view.findViewById<TextInputEditText>(R.id.etLotNumber)
        val etPaletNumber = view.findViewById<TextInputEditText>(R.id.etPaletNumber)
        val rvImages = view.findViewById<RecyclerView>(R.id.rvImages)
        val btnAdd = view.findViewById<Button>(R.id.btnAddImage)
        val btnUpload = view.findViewById<Button>(R.id.btnUpload)

        adapter = PaletImageAdapter(lotImgViewModel.typeImageLot.value.map { it.name ?: "" })
        rvImages.layoutManager = LinearLayoutManager(requireContext())
        rvImages.adapter = adapter

        btnAdd.setOnClickListener {
            imagePicker.launch("image/*")
        }

        btnUpload.setOnClickListener {
            val lotNumber = etLotNumber.text.toString()
            val paletNumber = etPaletNumber.text.toString()
            if (lotNumber.isBlank() or paletNumber.isBlank()) {
                Toast.makeText(requireContext(), "Debes ingresar lot_number y palet_number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            enviarDatos(lotNumber, paletNumber)
        }

        return view
    }

    private fun enviarDatos(lotNumber: String, paletNumber: String) {
        lifecycleScope.launch {
            val tokenResponse = apiService.PostToken("fabian", "Zarathustra40").body()
            val token = tokenResponse?.access ?: return@launch

            val parts = mutableListOf<MultipartBody.Part>()
            val tipos = mutableListOf<MultipartBody.Part>()

            val items = adapter.getItems()
            for (item in items) {
                val stream = requireContext().contentResolver.openInputStream(item.uri)!!
                val bytes = stream.readBytes()
                val reqFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                parts.add(
                    MultipartBody.Part.createFormData("imagen", "foto_${System.currentTimeMillis()}.jpg", reqFile)
                )
                tipos.add(
                    MultipartBody.Part.createFormData("tipo", item.tipo ?: "")
                )
            }
            Log.i("LotImagesFragment", "tipos: $tipos")

            val lotNumberPart = MultipartBody.Part.createFormData("lot_number", lotNumber)
            val paletNumberPart = MultipartBody.Part.createFormData("palet_number", paletNumber)

            val response = apiService.uploadPaletImages(
                authHeader = "Bearer $token",
                lotNumber = lotNumberPart,
                paletNumber = paletNumberPart,
                tipos = tipos,
                imagenes = parts
            )

            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Imágenes enviadas correctamente", Toast.LENGTH_SHORT).show()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Imagenes guardadas")
                    .setMessage("Las Imagenes se han guardado con éxito.")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                        findNavController().navigate(R.id.CompanySelectFragment)
                    }.show()
            } else {
                Toast.makeText(requireContext(), "Error al enviar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}