package com.example.vandrservices.ui.form.lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vandrservices.domain.model.Lot
import com.example.vandrservices.domain.repository.LotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LotListViewModel @Inject constructor(
    private val repository: LotRepository
) : ViewModel() {

    val lots: StateFlow<List<Lot>> =
        repository.getLots()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}