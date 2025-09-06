package com.example.vandrservices.ui.form.palet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vandrservices.domain.model.Palet
import com.example.vandrservices.domain.usecase.AddPaletUseCase
import com.example.vandrservices.domain.usecase.GetPaletsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaletCreationViewModel @Inject constructor(
    private val getPaletUseCase: GetPaletsUseCase,
    private val addPaletUseCase: AddPaletUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun savePalet(palet: Palet) {
        viewModelScope.launch {
            addPaletUseCase(palet)
        }
    }

    val palets = getPaletUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun serLotPersist(
        variety: String,
        lotId: Int,
        localLotId: String,
        grower: String,
        packDate: String,
        cases: String,
        label: String
    ) {
        savedStateHandle["variety"] = variety
        savedStateHandle["lotId"] = lotId
        savedStateHandle["localLotId"] = localLotId
        savedStateHandle["grower"] = grower
        savedStateHandle["packDate"] = packDate
        savedStateHandle["cases"] = cases
        savedStateHandle["label"] = label
    }

    fun getLotPersist(): Map<String, Any?> {
        return mapOf(
            "variety" to savedStateHandle.get<String>("variety"),
            "lotId" to savedStateHandle.get<Int>("lotId"),
            "localLotId" to savedStateHandle.get<String>("localLotId"),
            "grower" to savedStateHandle.get<String>("grower"),
            "packDate" to savedStateHandle.get<String>("packDate"),
            "cases" to savedStateHandle.get<String>("cases"),
            "label" to savedStateHandle.get<String>("label")
        )
    }
}