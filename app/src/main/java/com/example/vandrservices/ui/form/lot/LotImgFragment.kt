package com.example.vandrservices.ui.form.lot

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vandrservices.R
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.databinding.FragmentLotImgBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LotImagesFragment : Fragment() {

    private lateinit var adapter: LotImageAdapter
    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris.forEach { adapter.addImage(it) }
    }

    private val apiService by lazy { RetrofitControles.getRetrofit().create(ApiService::class.java) }
    private val lotImgViewModel by viewModels<LotImgViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_lot_img, container, false)
        val etLotNumber = view.findViewById<TextInputEditText>(R.id.etLotNumber)
        val rvImages = view.findViewById<RecyclerView>(R.id.rvImages)
        val btnAdd = view.findViewById<Button>(R.id.btnAddImage)
        val btnUpload = view.findViewById<Button>(R.id.btnUpload)

        adapter = LotImageAdapter(lotImgViewModel.typeImageLot.value.map { it.name ?: "" })
        rvImages.layoutManager = LinearLayoutManager(requireContext())
        rvImages.adapter = adapter

        btnAdd.setOnClickListener {
            imagePicker.launch("image/*")
        }

        btnUpload.setOnClickListener {
            val lotNumber = etLotNumber.text.toString()
            if (lotNumber.isBlank()) {
                Toast.makeText(requireContext(), "Debes ingresar lot_number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            enviarDatos(lotNumber)
        }

        return view
    }

    private fun enviarDatos(lotNumber: String) {
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

            val response = apiService.uploadLotImages(
                authHeader = "Bearer $token",
                lotNumber = lotNumberPart,
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
