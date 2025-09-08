package com.example.vandrservices.ui.form.damage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vandrservices.domain.model.Damage
import com.example.vandrservices.domain.usecase.AddDamageUseCase
import com.example.vandrservices.domain.usecase.GetDamagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DamageCreationViewModel @Inject constructor(
    private val getDamageUseCase: GetDamagesUseCase,
    private val addDamageUseCase: AddDamageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun saveDamage(damage: Damage) {
        viewModelScope.launch {
            addDamageUseCase(damage)
        }
    }

    val damage = getDamageUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun serPaletPersist(
        fruitName: String,
        lotId: Int,
        paletId: Int,
        localLotId : String,
        grower: String,
        cases: String,
        label: String
    ) {
        savedStateHandle["fruitName"] = fruitName
        savedStateHandle["lotId"] = lotId
        savedStateHandle["paletId"] = paletId
        savedStateHandle["localLotId"] = localLotId
        savedStateHandle["grower"] = grower
        savedStateHandle["cases"] = cases
        savedStateHandle["label"] = label
    }

    fun getPaletPersist(): Map<String, Any?> {
        return mapOf(
            "fruitName" to savedStateHandle.get<String>("fruitName"),
            "lotId" to savedStateHandle.get<Int>("lotId"),
            "paletId" to savedStateHandle.get<Int>("paletId"),
            "localLotId" to savedStateHandle.get<Int>("localLotId"),
            "grower" to savedStateHandle.get<String>("grower"),
            "cases" to savedStateHandle.get<String>("cases"),
            "label" to savedStateHandle.get<String>("label")
        )
    }
}