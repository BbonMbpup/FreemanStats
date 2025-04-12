package com.example.freemanstats.model

import com.google.gson.annotations.SerializedName

data class ClanWar(
    @SerializedName("state") val state: String,
    @SerializedName("teamSize") val teamSize: Int,
    @SerializedName("clan") val clan: Clan,
    @SerializedName("opponent") val opponent: Clan
)