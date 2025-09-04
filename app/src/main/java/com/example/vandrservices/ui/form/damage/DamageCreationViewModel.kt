package com.example.vandrservices.ui.form.damage

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
) : ViewModel() {

    fun saveDamage(damage: Damage) {
        viewModelScope.launch {
            addDamageUseCase(damage)
        }
    }

    val damage = getDamageUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}