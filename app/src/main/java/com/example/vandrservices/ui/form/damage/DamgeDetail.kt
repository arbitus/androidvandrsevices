package com.example.vandrservices.ui.form.damage

enum class DamageType {
    QUALITY,
    CONDITION
}

data class DamageField(
    val name: String,
    val type: DamageType,
    var value: Double = 0.0
)