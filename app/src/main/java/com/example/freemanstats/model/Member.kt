package com.example.freemanstats.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("tag") val tag: String,
    @SerializedName("name") val name: String,
    @SerializedName("townhallLevel") val townhallLevel: Int,
    @SerializedName("mapPosition") val mapPosition: Int,
    @SerializedName("attacks") val attacks: List<Attack>?,
    @SerializedName("opponentAttacks") val opponentAttacks: Int,
    @SerializedName("bestOpponentAttack") val bestOpponentAttack: Attack?
)