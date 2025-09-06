package com.example.vandrservices.ui.form.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.vandrservices.domain.model.Lot
import com.example.vandrservices.domain.model.VarietyInfo
import com.example.vandrservices.domain.usecase.AddLotUseCase
import com.example.vandrservices.domain.usecase.GetLotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotCreationViewModel @Inject constructor(private val addLotUseCase: AddLotUseCase, private val getLotsUseCase: GetLotsUseCase, private val savedStateHandle: SavedStateHandle) : ViewModel()  {
    private var _varietys = MutableStateFlow<List<VarietyInfo>>(emptyList())
    val varietys: StateFlow<List<VarietyInfo>> = _varietys

    init {
        _varietys.value = listOf(
            VarietyInfo.Banana,
            VarietyInfo.Coco,
            VarietyInfo.Berrie,
            VarietyInfo.Jicama,
            VarietyInfo.Purple,
            VarietyInfo.Aloe_Vera,
            VarietyInfo.Avocado,
            VarietyInfo.Blueberrie,
            VarietyInfo.Chirimoya,
            VarietyInfo.Lemon,
            VarietyInfo.Mangoes,
            VarietyInfo.Strawberrie,
            VarietyInfo.Raspberries,
            VarietyInfo.Tomato,
            VarietyInfo.Peppers
        )
    }
    fun addLot(lot: Lot) {
        viewModelScope.launch {
            addLotUseCase(lot)
        }
    }
    val lots = getLotsUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setEmpresaId(id: Int) {
        savedStateHandle["empresaId"] = id
    }

    fun getEmpresaId(): Int? = savedStateHandle.get<Int>("empresaId")

}