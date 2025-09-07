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
class LotCreationViewModel @Inject constructor(
    private val addLotUseCase: AddLotUseCase,
    private val getLotsUseCase: GetLotsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _varietys = MutableStateFlow<List<VarietyInfo>>(emptyList())
    val varietys: StateFlow<List<VarietyInfo>> = _varietys

    init {
        _varietys.value = listOf(
            VarietyInfo.Aloe_Vera,
            VarietyInfo.Avocado,
            VarietyInfo.Banana,
            VarietyInfo.Banana_Organic,
            VarietyInfo.Berrie,
            VarietyInfo.Betabel,
            VarietyInfo.Blueberrie,
            VarietyInfo.Brocoli,
            VarietyInfo.Calabacita,
            VarietyInfo.Carambola,
            VarietyInfo.Carrot,
            VarietyInfo.Cauliflower,
            VarietyInfo.Chayote,
            VarietyInfo.Chirimoya,
            VarietyInfo.Cilantro,
            VarietyInfo.Coco,
            VarietyInfo.Elote,
            VarietyInfo.Esparrago,
            VarietyInfo.Garbanzo,
            VarietyInfo.Garlic,
            VarietyInfo.Granada,
            VarietyInfo.Green_Onions,
            VarietyInfo.Guayaba,
            VarietyInfo.Hoja_Platano,
            VarietyInfo.Jackfruit,
            VarietyInfo.Jicama,
            VarietyInfo.Lemon,
            VarietyInfo.Lettuce,
            VarietyInfo.Malanga,
            VarietyInfo.Mangoes,
            VarietyInfo.Mangosteen,
            VarietyInfo.Melons,
            VarietyInfo.Naranja,
            VarietyInfo.Nopal,
            VarietyInfo.Onions,
            VarietyInfo.Papalo,
            VarietyInfo.Papaya,
            VarietyInfo.Peppers,
            VarietyInfo.Pepino,
            VarietyInfo.Pera,
            VarietyInfo.Pina,
            VarietyInfo.Pitahaya,
            VarietyInfo.Plantains,
            VarietyInfo.Purple,
            VarietyInfo.Rambutan,
            VarietyInfo.Raspberries,
            VarietyInfo.Strawberrie,
            VarietyInfo.Tejocote,
            VarietyInfo.Tomatillo,
            VarietyInfo.Tomato,
            VarietyInfo.Tuna,
            VarietyInfo.Verdolaga,
            VarietyInfo.Watermelons
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