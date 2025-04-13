package com.example.freemanstats.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "clan_wars")
data class ClanWar(
    @SerializedName("state") val state: String,
    @SerializedName("teamSize") val teamSize: Int,
    @SerializedName("clan") val clan: Clan,
    @SerializedName("opponent") val opponent: Clan
)