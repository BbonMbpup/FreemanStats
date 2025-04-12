package com.example.freemanstats.model

import com.google.gson.annotations.SerializedName

data class Attack(
    @SerializedName("attackerTag") val attackerTag: String,
    @SerializedName("defenderTag") val defenderTag: String,
    @SerializedName("stars") val stars: Int,
    @SerializedName("destructionPercentage") val destructionPercentage: Double,
    @SerializedName("order") val order: Int,
    @SerializedName("duration") val duration: Int
)