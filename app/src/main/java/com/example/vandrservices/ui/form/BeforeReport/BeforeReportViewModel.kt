package com.example.vandrservices.ui.form.BeforeReport

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BeforeReportViewModel@Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    fun setLotPersist(variety: String, lotId: Int, localLotId: String, grower: String, packDate: String, cases: String, label: String) {
        savedStateHandle["variety"] = variety
        savedStateHandle["lotId"] = lotId
        savedStateHandle["localLotId"] = localLotId
        savedStateHandle["grower"] = grower
        savedStateHandle["packDate"] = packDate
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