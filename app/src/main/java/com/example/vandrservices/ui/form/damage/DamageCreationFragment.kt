package com.example.vandrservices.ui.form.damage

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vandrservices.R
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.domain.model.Damage
import com.example.vandrservices.domain.model.DamageToJson
import com.example.vandrservices.domain.model.User
import com.example.vandrservices.ui.form.isInternetAvailable
import com.example.vandrservices.ui.login.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.util.UUID

@AndroidEntryPoint
class DamageCreationFragment : Fragment() {



    private val fieldsByFruit = mapOf(
        "Banana" to listOf(
            DamageField("Scars", DamageType.QUALITY),
            DamageField("Latex", DamageType.QUALITY),
            DamageField("Healed Wounds", DamageType.QUALITY),
            DamageField("Broken Neck", DamageType.CONDITION),
            DamageField("Skin Abrasion", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Insect Damage", DamageType.CONDITION),
            DamageField("Ripe", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Lemon" to listOf(
            DamageField("Scars / Russet", DamageType.QUALITY),
            DamageField("Oleocellosis", DamageType.QUALITY),
            DamageField("Misshape", DamageType.QUALITY),
            DamageField("Sunburn", DamageType.QUALITY),
            DamageField("Degreening", DamageType.QUALITY),
            DamageField("Skin Defects", DamageType.QUALITY),
            DamageField("Wounds / Mechanical Damage", DamageType.CONDITION),
            DamageField("Bruises / Soft", DamageType.CONDITION),
            DamageField("SEB", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Shrivel", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Berrie" to listOf(
            DamageField("Red Cells", DamageType.QUALITY),
            DamageField("Mechanical Damage", DamageType.QUALITY),
            DamageField("White Cells", DamageType.QUALITY),
            DamageField("Dry Cells", DamageType.QUALITY),
            DamageField("Shrivel", DamageType.CONDITION),
            DamageField("Leaking", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Soft Fruit", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION),
        ),
        "Blueberrie" to listOf(
            DamageField("Scars", DamageType.QUALITY),
            DamageField("Immature", DamageType.QUALITY),
            DamageField("Pulled Stem", DamageType.CONDITION),
            DamageField("Soft Fruit", DamageType.CONDITION),
            DamageField("Shrivel", DamageType.CONDITION),
            DamageField("Leaking", DamageType.CONDITION),
            DamageField("Overripe", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Avocado" to listOf(
            DamageField("Turning Color", DamageType.QUALITY),
            DamageField("Black Spot", DamageType.QUALITY),
            DamageField("Misshape", DamageType.QUALITY),
            DamageField("Scars / Russet", DamageType.QUALITY),
            DamageField("Surface Discoloration", DamageType.QUALITY),
            DamageField("Sunburn", DamageType.CONDITION),
            DamageField("Wounds", DamageType.CONDITION),
            DamageField("Ripe", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Insect Damage", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Mangoes" to listOf(
            DamageField("Scars", DamageType.QUALITY),
            DamageField("Discoloration", DamageType.QUALITY),
            DamageField("Lenticels", DamageType.QUALITY),
            DamageField("Sap Burn", DamageType.QUALITY),
            DamageField("Latex", DamageType.QUALITY),
            DamageField("Sunken", DamageType.QUALITY),
            DamageField("Anthracnose", DamageType.CONDITION),
            DamageField("Bruises", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION),
            DamageField("Immature", DamageType.CONDITION)
        ),
        "Peppers" to listOf(
            DamageField("Scars / Russet", DamageType.QUALITY),
            DamageField("Misshape", DamageType.QUALITY),
            DamageField("Turning Color", DamageType.QUALITY),
            DamageField("Pulled Stem", DamageType.QUALITY),
            DamageField("Sunburn", DamageType.QUALITY),
            DamageField("Wounds", DamageType.CONDITION),
            DamageField("Bruises", DamageType.CONDITION),
            DamageField("Soft", DamageType.CONDITION),
            DamageField("Shrivel", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Insect Damage", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Raspberries" to listOf(
            DamageField("Broken Cells", DamageType.QUALITY),
            DamageField("White Cells", DamageType.QUALITY),
            DamageField("Water Cells", DamageType.QUALITY),
            DamageField("Immature", DamageType.QUALITY),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Soft Fruit", DamageType.CONDITION),
            DamageField("Shrivel", DamageType.CONDITION),
            DamageField("Leaking", DamageType.CONDITION),
            DamageField("Overripe", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Tomato" to listOf(
            DamageField("Scars / Russet", DamageType.QUALITY),
            DamageField("Oleocellosis", DamageType.QUALITY),
            DamageField("Misshape", DamageType.QUALITY),
            DamageField("Sunburn", DamageType.QUALITY),
            DamageField("Degreening", DamageType.QUALITY),
            DamageField("Skin Defects", DamageType.QUALITY),
            DamageField("Wounds / Mechanical Damage", DamageType.CONDITION),
            DamageField("Bruises / Soft", DamageType.CONDITION),
            DamageField("SEB", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Shrivel", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Jackfruit" to listOf(
            DamageField("Scars / Russet", DamageType.QUALITY),
            DamageField("Broken", DamageType.QUALITY),
            DamageField("Misshape", DamageType.QUALITY),
            DamageField("Wounds", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Insect Damage", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Malanga" to listOf(
            DamageField("Scars", DamageType.QUALITY),
            DamageField("Skin Defects", DamageType.QUALITY),
            DamageField("Wounds", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Insect Damage", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Papaya" to listOf(
            DamageField("Scars", DamageType.QUALITY),
            DamageField("Skin Defects", DamageType.QUALITY),
            DamageField("Sunburn", DamageType.QUALITY),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Insect Damage", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        ),
        "Chayote" to listOf(
            DamageField("Scars / Russet", DamageType.QUALITY),
            DamageField("Skin Defects", DamageType.QUALITY),
            DamageField("Misshape", DamageType.QUALITY),
            DamageField("Bruises", DamageType.CONDITION),
            DamageField("Freeze Damage", DamageType.CONDITION),
            DamageField("Insect Damage", DamageType.CONDITION),
            DamageField("Mold", DamageType.CONDITION),
            DamageField("Decay", DamageType.CONDITION)
        )
    )

    companion object {
        private const val ARG_FRUIT = "variety"

        fun newInstance(fruit: String) = DamageCreationFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FRUIT, fruit)
            }
        }
    }
    private val viewModel: DamageCreationViewModel by viewModels<DamageCreationViewModel>()
    private lateinit var containerLayout: LinearLayout
    private lateinit var retrofit: Retrofit
    private val apiService by lazy { retrofit.create(ApiService::class.java) }
    private val userViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_damage_creation, container, false)
        containerLayout = root.findViewById(R.id.dynamic_form_container)

        var fruitName = arguments?.getString(ARG_FRUIT)
        val paletIdString = arguments?.getString("id")?: "0"
        var paletId = paletIdString.toIntOrNull()?: 0
        viewModel.serPaletPersist(fruitName ?: "", paletId)
        val resultPersist = viewModel.getPaletPersist()
        fruitName = resultPersist.first
        paletId = resultPersist.second ?: 0
        val fields = fieldsByFruit[fruitName] ?: emptyList()
        Log.i("fruit name", fields.toString())
        Log.i("palet Id", paletId.toString())

        retrofit = RetrofitControles.getRetrofit()
        createDynamicForm(fields)
        root.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            val damages = readFormData()
            if (isInternetAvailable(requireContext())) {
                saveDamage(damages, paletId)
            }else{
                saveDamageLocal(damages, paletId)
            }
            observeDamages()
        }
        return root
    }

    private fun createDynamicForm(fields: List<DamageField>) {
        containerLayout.removeAllViews()

        fields.forEach { damage ->
            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 8 }
            }

            // Campo de texto para el nombre
            val nameField = TextInputEditText(requireContext()).apply {
                setText(damage.name)
                isEnabled = false // No editable si solo es para mostrar
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
            }

            // CheckBox para Condition
            val conditionCheck = CheckBox(requireContext()).apply {
                text = "Condition"
                isChecked = damage.type == DamageType.CONDITION
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            // CheckBox para Quality
            val qualityCheck = CheckBox(requireContext()).apply {
                text = "Quality"
                isChecked = damage.type == DamageType.QUALITY
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            // Sincronizar para que solo uno se marque
            conditionCheck.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) qualityCheck.isChecked = false
            }
            qualityCheck.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) conditionCheck.isChecked = false
            }

            // Campo para valor
            val valueField = TextInputEditText(requireContext()).apply {
                hint = "Valor"
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                setText(if (damage.value != 0.0) damage.value.toString() else "0")
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            // Agregar vistas al layout
            itemLayout.addView(nameField)
            itemLayout.addView(conditionCheck)
            itemLayout.addView(qualityCheck)
            itemLayout.addView(valueField)

            containerLayout.addView(itemLayout)
        }
    }

    private fun readFormData(): List<Triple<String, String, Double>> {
        val result = mutableListOf<Triple<String, String, Double>>()

        for (i in 0 until containerLayout.childCount) {
            val row = containerLayout.getChildAt(i) as LinearLayout

            val name = (row.getChildAt(0) as TextInputEditText).text.toString()
            val isCondition = (row.getChildAt(1) as CheckBox).isChecked
            val type = if (isCondition) "Condition" else "Quality"
            val value = (row.getChildAt(3) as TextInputEditText).text.toString().toDoubleOrNull() ?: 0.0

            result.add(Triple(name, type, value))
        }

        return result
    }
    private fun saveDamage(damageList: List<Triple<String, String, Double>>, paletId: Int) {
        lifecycleScope.launch {
            val user: User? = userViewModel.usersFlow.firstOrNull()?.firstOrNull()
            val token = user?.token
            if (!token.isNullOrEmpty()) {
                for (damage in damageList) {
                    val damageJson = DamageToJson(
                        palet = paletId,
                        name = damage.first,
                        type = damage.second,
                        value = damage.third
                    )
                    val responseDamage = apiService.createDamage("Bearer $token", damageJson )
                    if (responseDamage.isSuccessful) {
                        Log.i("DamageCreate", "Damage enviado correctamente al servidor. id: ${responseDamage.body()}")
                    }
                    else {
                        Log.e("DamageCreate", "Error al enviar Damage al servidor. Código de respuesta: ${responseDamage.code()}")
                    }
                }
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Damages guardado")
                    .setMessage("Los Damage se han guardado con éxito.")
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                        findNavController().navigate(R.id.CompanySelectFragment)
                    }.show()
            }
        }
    }
    private fun saveDamageLocal(damageList: List<Triple<String, String, Double>>, paletId: Int) {
        for (damage in damageList) {
            val damageToSave = Damage(
                localId = "${UUID.randomUUID()}",
                palet = paletId,
                name = damage.first,
                type = damage.second,
                value = damage.third,
            )

            viewModel.saveDamage(damageToSave)

        }
    }

    private fun observeDamages() {
        lifecycleScope.launch {
            viewModel.damage.collectLatest { damages ->
                Log.i("DamageFragment", "Damages almacenados: $damages")
            }
        }
    }
}
