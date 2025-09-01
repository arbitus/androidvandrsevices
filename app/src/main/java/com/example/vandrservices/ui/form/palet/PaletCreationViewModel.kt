package com.example.vandrservices.ui.form.palet

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
) : ViewModel() {

    fun savePalet(palet: Palet) {
        viewModelScope.launch {
            addPaletUseCase(palet)
        }
    }

    val palets = getPaletUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}