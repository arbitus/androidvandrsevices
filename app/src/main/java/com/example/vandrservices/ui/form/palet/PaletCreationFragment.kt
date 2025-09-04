package com.example.vandrservices.ui.form.palet

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vandrservices.R
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.domain.model.Palet
import com.example.vandrservices.domain.model.PaletToJson
import com.example.vandrservices.domain.model.User
import com.example.vandrservices.ui.form.isInternetAvailable
import com.example.vandrservices.ui.login.LoginViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID


@AndroidEntryPoint
class PaletCreationFragment : Fragment() {

    private val viewModel: PaletCreationViewModel by viewModels<PaletCreationViewModel>()

    private lateinit var containerLayout: LinearLayout

    private lateinit var retrofit: Retrofit
    private val apiService by lazy { retrofit.create(ApiService::class.java) }

    private val fieldsByFruit = mapOf(
        "Banana" to listOf(
            "paletNumber",
            "variety",
            "packageSize",
            "boxes",
            "ageAtInspection",
            "label",
            "openAppearance",
            "externalColor",
            "cat",
            "weight",
            "pulpTemperature",
            "packDate",
            "gradeMaximum",
            "gradeMinimum",
            "lengthMaximum",
            "lengthMinimum",
            "c7_8",
            "c8_9",
            "c9_10",
            "c10_11",
            "c39",
            "c40_49",
            "c50",
            "c2_dedos",
            "c3_dedos",
            "c4_dedos",
            "c5_dedos",
            "c6_dedos",
            "c7_dedos",
            "c8_dedos",
            "c9_dedos",
            "c10_dedos",
            "c11_dedos"
        ),
        "Lemon" to listOf(
            "paletNumber",
            "variety",
            "packageSize",
            "traceability",
            "boxes",
            "pulpTemperature",
            "label",
            "countMin",
            "countMax",
            "openAppearance",
            "externalColor",
            "cat",
            "weight",
            "mixdSize"
        ),
        "Berrie" to listOf(
            "paletNumber",
            "variety",
            "grower",
            "packageSize",
            "boxes",
            "packDate",
            "label",
            "openAppearance",
            "weight",
            "pulpTemperature"
        ),
        "Blueberrie" to listOf(
            "paletNumber",
            "variety",
            "grower",
            "packageSize",
            "boxes",
            "packDate",
            "label",
            "openAppearance",
            "weight",
            "pulpTemperature",
            "bloom"
        ),
        "Avocado" to listOf(
            "paletNumber",
            "variety",
            "packageSize",
            "boxes",
            "packDate",
            "label",
            "pressurePSI",
            "openAppearance",
            "externalColor"
        ),
        "Mangoes" to listOf(
            "paletNumber",
            "variety",
            "mixdSize",
            "packageSize",
            "boxes",
            "packDate",
            "cases",
            "grower",
            "label",
            "openAppearance",
            "groundColorGreen",
            "groundColorTurning",
            "groundColorYellow",
            "blushAverage",
            "cblush",
            "firmnessHard",
            "firmnessSensitive",
            "firmnessSoft",
            "plu",
            "brix",
            "count"
        ),
        "Peppers" to listOf(
            "paletNumber",
            "variety",
            "packageSize",
            "traceability",
            "boxes",
            "pulpTemperature",
            "label",
            "countMin",
            "countMax",
            "openAppearance",
            "externalColor",
            "cat",
            "weight",
            "mixdSize",
            "count"
        ),
        "Raspberries" to listOf(
            "paletNumber",
            "variety",
            "grower",
            "packageSize",
            "boxes",
            "packDate",
            "label",
            "openAppearance",
            "weight",
            "pulpTemperature"
        ),
        "Tomato" to listOf(
            "paletNumber",
            "variety",
            "packageSize",
            "traceability",
            "boxes",
            "pulpTemperature",
            "label",
            "countMin",
            "countMax",
            "openAppearance",
            "externalColor",
            "cat",
            "weight",
            "mixdSize",
            "count"
        )
    )

    private val inputViews = mutableMapOf<String, EditText>()
    private val userViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_palet_creation, container, false)
        containerLayout = view.findViewById(R.id.dynamic_form_container)
        val variety = arguments?.getString("variety") ?: "Banana"
        val lotId = arguments?.getInt("lotId") ?: 0
        val localLotId = arguments?.getString("localLotId") ?: ""
        val grower = arguments?.getString("grower") ?: ""
        val packDate = arguments?.getString("packDate") ?: ""
        val cases = arguments?.getString("cases") ?: 0
        val label = arguments?.getString("label") ?: ""
        Log.i("lotId", "lotId: $lotId")
        retrofit = RetrofitControles.getRetrofit()
        setupForm(variety, grower.toString(), packDate, cases.toString(), label)
        view.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            savePalet(variety, lotId, localLotId)
        }

        observePalets()

        return view
    }

    private fun setupForm(variety: String, grower: String, packDate: String, cases: String, label: String) {
        containerLayout.removeAllViews()
        val fields = fieldsByFruit[variety] ?: emptyList()

        val cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            12f,
            resources.displayMetrics
        )

        // Imagen superior (opcional)
        val imageView = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_loogitpo) // reemplaza por tu drawable
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                bottomMargin = 24
            }
        }
        containerLayout.addView(imageView)

        fields.forEach { field ->
            val textInputLayout = TextInputLayout(
                requireContext(),
                null,
                com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox
            ).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 0) // márgenes laterales y entre campos
                }
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                setBoxCornerRadii(cornerRadius, cornerRadius, cornerRadius, cornerRadius)
                hint = field.replaceFirstChar { it.uppercaseChar() }
            }

            val editText = TextInputEditText(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                // Tipo de input dinámico según nombre del campo
                inputType = when {
                    field.contains("count", ignoreCase = true) -> InputType.TYPE_CLASS_NUMBER
                    field.contains("grower", ignoreCase = true) -> InputType.TYPE_CLASS_NUMBER
                    field.contains("boxes", ignoreCase = true) ||
                            field.contains("cases", ignoreCase = true) ||
                            field.contains("c7_8", ignoreCase = true) ||
                            field.contains("c8_9", ignoreCase = true) ||
                            field.contains("c9_10", ignoreCase = true) ||
                            field.contains("c10_11", ignoreCase = true) ||
                            field.contains("c39", ignoreCase = true) ||
                            field.contains("c40_49", ignoreCase = true) ||
                            field.contains("c50", ignoreCase = true) ||
                            field.contains("c2_dedos", ignoreCase = true) ||
                            field.contains("c3_dedos", ignoreCase = true) ||
                            field.contains("c4_dedos", ignoreCase = true) ||
                            field.contains("c5_dedos", ignoreCase = true) ||
                            field.contains("c6_dedos", ignoreCase = true) ||
                            field.contains("c7_dedos", ignoreCase = true) ||
                            field.contains("c8_dedos", ignoreCase = true) ||
                            field.contains("c9_dedos", ignoreCase = true) ||
                            field.contains("c10_dedos", ignoreCase = true) ||
                            field.contains("c11_dedos", ignoreCase = true) ||
                            field.contains("weight", ignoreCase = true) ->
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    field.contains("mixdSize", ignoreCase = true) ||
                            field.contains("gradeMaximum", ignoreCase = true) ||
                            field.contains("gradeMinimum", ignoreCase = true) ||
                            field.contains("lengthMaximum", ignoreCase = true) ||
                            field.contains("lengthMinimum", ignoreCase = true) ||
                            field.contains("pulpTemperature", ignoreCase = true) ->
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

                    field.contains("date", ignoreCase = true) -> {
                        // Evitar teclado y usar date picker
                        InputType.TYPE_NULL
                    }

                    else -> InputType.TYPE_CLASS_TEXT
                }
                val growerInt: Int = grower.toIntOrNull() ?: 0
                when {
                    field.equals("grower", ignoreCase = true) && growerInt != 0 ->
                        setText(growerInt.toString())
                    field.equals("cases", ignoreCase = true) && cases.toInt() != 0 ->
                        setText(cases)
                    field.equals("date", ignoreCase = true) && packDate.isNotEmpty() ->
                        setText(packDate)
                    field.equals("label", ignoreCase = true) && label.isNotEmpty() ->
                        setText(label)
                }
                if (field.contains("date", ignoreCase = true)) {
                    isFocusable = false
                    setOnClickListener {
                        val picker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Seleccionar fecha")
                            .build()
                        picker.show(parentFragmentManager, "datePicker")
                        picker.addOnPositiveButtonClickListener { millis ->
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            setText(sdf.format(Date(millis)))
                        }
                    }
                }
            }

            textInputLayout.addView(editText)
            containerLayout.addView(textInputLayout)

            inputViews[field] = editText
        }
    }


    private fun savePalet(variety: String, lotId: Int, localLotId: String) {
        // Helper para leer texto limpio
        fun getText(field: String): String? {
            val value = inputViews[field]?.text?.toString()?.trim()
            return value.takeIf { !it.isNullOrEmpty() }
        }

        // Helper para leer enteros opcionales
        fun getInt(field: String): Int? = getText(field)?.toIntOrNull()

        // Helper para leer dobles opcionales
        fun getDouble(field: String): Double? = getText(field)?.toDoubleOrNull()

        // Validaciones mínimas (ejemplo: número de palet obligatorio)
        val paletNumber = getText("paletNumber") ?: run {
            Toast.makeText(
                requireContext(),
                "El número de palet es obligatorio",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val palet = Palet(
            localId = (0..Int.MAX_VALUE).random().toString(),
            lotId = localLotId,
            paletNumber = paletNumber,
            variedad = variety,
            variety = getText("variety"),
            grower = getInt("grower"),
            boxes = getInt("boxes"),
            cases = getInt("cases"),
            weight = getDouble("weight"),
            pressurePSI = getText("pressurePSI"),
            brix = getText("brix"),
            packageSize = getText("packageSize"),
            packDate = getText("packDate"),
            traceability = getText("traceability"),
            ageAtInspection = getText("ageAtInspection"),
            sack = getText("sack"),
            label = getText("label"),
            openAppearance = getText("openAppearance"),
            internalColor = getText("internalColor"),
            externalColor = getText("externalColor"),
            count = getInt("count"),
            cat = getText("cat"),
            mixdSize = getDouble("mixdSize"),
            pulpTemperature = getDouble("pulpTemperature"),
            gradeMaximum = getDouble("gradeMaximum"),
            gradeMinimum = getDouble("gradeMinimum"),
            lengthMaximum = getDouble("lengthMaximum"),
            lengthMinimum = getDouble("lengthMinimum"),
            bloom = getText("bloom"),
            groundColorGreen = getText("groundColorGreen"),
            groundColorTurning = getText("groundColorTurning"),
            groundColorYellow = getText("groundColorYellow"),
            blushAverage = getText("blushAverage"),
            cblush = getText("cblush"),
            firmnessHard = getText("firmnessHard"),
            firmnessSensitive = getText("firmnessSensitive"),
            firmnessSoft = getText("firmnessSoft"),
            plu = getText("plu"),
            countMin = getInt("countMin"),
            countMax = getInt("countMax"),
            c7_8 = getInt("c7_8"),
            c8_9 = getInt("c8_9"),
            c9_10 = getInt("c9_10"),
            c10_11 = getInt("c10_11"),
            c39 = getInt("c39"),
            c40_49 = getInt("c40_49"),
            c50 = getInt("c50"),
            c2_dedos = getInt("c2_dedos"),
            c3_dedos = getInt("c3_dedos"),
            c4_dedos = getInt("c4_dedos"),
            c5_dedos = getInt("c5_dedos"),
            c6_dedos = getInt("c6_dedos"),
            c7_dedos = getInt("c7_dedos"),
            c8_dedos = getInt("c8_dedos"),
            c9_dedos = getInt("c9_dedos"),
            c10_dedos = getInt("c10_dedos"),
            c11_dedos = getInt("c11_dedos"),
            commodity = getText("commodity"),
            totalCount = getInt("totalCount"),
            totalQuality = 0.0,
            totalCondition = 0.0,
            qualityColor = "",
            conditionColor = "",
            qualityValue = "",
            conditionValue = ""
        )
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(palet.packDate?: "")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val packDate = outputFormat.format(parsedDate!!)
        val paletToJson = PaletToJson(
            id = null,
            lot = lotId,
            paletNumber = palet.paletNumber,
            variety = palet.variety,
            variedad = palet.variedad,
            grower = palet.grower,
            packageSize = palet.packageSize,
            packDate = packDate,
            traceability = palet.traceability,
            ageAtInspection = palet.ageAtInspection,
            boxes = palet.boxes,
            sack = palet.sack,
            cases = palet.cases,
            label = palet.label,
            pressurePSI = palet.pressurePSI,
            brix = palet.brix,
            openAppearance = palet.openAppearance,
            internalColor = palet.internalColor,
            externalColor = palet.externalColor,
            count = palet.count,
            cat = palet.cat,
            weight = palet.weight,
            mixdSize = palet.mixdSize,
            pulpTemperature = palet.pulpTemperature,
            gradeMaximum = palet.gradeMaximum,
            gradeMinimum = palet.gradeMinimum,
            lengthMaximum = palet.lengthMaximum,
            lengthMinimum = palet.lengthMinimum,
            bloom = palet.bloom,
            groundColorGreen = palet.groundColorGreen,
            groundColorTurning = palet.groundColorTurning,
            groundColorYellow = palet.groundColorYellow,
            blushAverage = palet.blushAverage,
            cblush = palet.cblush,
            firmnessHard = palet.firmnessHard,
            firmnessSensitive = palet.firmnessSensitive,
            firmnessSoft = palet.firmnessSoft,
            plu = palet.plu,
            countMin = palet.countMin,
            countMax = palet.countMax,
            totalQuality = palet.totalQuality,
            totalCondition = palet.totalCondition,
            qualityColor = palet.qualityColor,
            conditionColor = palet.conditionColor,
            qualityValue = palet.qualityValue,
            conditionValue = palet.conditionValue,
            c7_8 = palet.c7_8,
            c8_9 = palet.c8_9,
            c9_10 = palet.c9_10,
            c10_11 = palet.c10_11,
            c39 = palet.c39,
            c40_49 = palet.c40_49,
            c50 = palet.c50,
            c_2_dedos = palet.c2_dedos,
            c_3_dedos = palet.c3_dedos,
            c_4_dedos = palet.c4_dedos,
            c_5_dedos = palet.c5_dedos,
            c_6_dedos = palet.c6_dedos,
            c_7_dedos = palet.c7_dedos,
            c_8_dedos = palet.c8_dedos,
            c_9_dedos = palet.c9_dedos,
            c_10_dedos = palet.c10_dedos,
            c_11_dedos = palet.c11_dedos,
            commodity = palet.commodity,
            totalCount = palet.totalCount
        )
        if (isInternetAvailable(requireContext())) {
            lifecycleScope.launch {
                val user: User? = userViewModel.usersFlow.firstOrNull()?.firstOrNull()
                val token = user?.token
                if (!token.isNullOrEmpty()) {
                    val responsePalet = apiService.createPalet("Bearer $token", paletToJson)
                    if (responsePalet.isSuccessful) {
                        Log.i("PaletCreateFragment", "Palet enviado correctamente al servidor. id: ${responsePalet.body()?.id}")
                        var paletId = responsePalet.body()?.id ?: 0
                        val bundle = bundleOf(
                            "variety" to variety,
                            "id" to paletId
                        )
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Palet guardado")
                            .setMessage("El palet se ha guardado con éxito.")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                                findNavController().navigate(R.id.damageCreationFragment, bundle)
                            }.show()
                    } else {
                        Log.e("PaletCreateFragment", "Error al enviar lote: ${responsePalet.code()}")
                    }
                }
            }
        }else {
            viewModel.savePalet(palet)
            val bundle = bundleOf(
                "variety" to variety,
                "id" to palet.localId
            )
            Log.i("PaletCreateFragment", "Palet guardado correctamente con id: ${palet.localId}")
            observePalets()
            findNavController().navigate(R.id.damageCreationFragment, bundle)
            Toast.makeText(requireContext(), "Palet guardado correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observePalets() {
        lifecycleScope.launch {
            viewModel.palets.collectLatest { palets ->
                Log.i("PaletFragment", "Palets almacenados: $palets")
            }
        }
    }
}