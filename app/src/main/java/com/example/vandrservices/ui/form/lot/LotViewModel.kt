package com.example.vandrservices.ui.form.lot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LotSharedViewModel : ViewModel() {
    val lotData = MutableLiveData(LotFormData())

    fun updateData(update: LotFormData) {
        lotData.value = update
    }
}