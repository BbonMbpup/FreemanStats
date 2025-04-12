package com.example.freemanstats.model

import com.google.gson.annotations.SerializedName

data class Clan(
    @SerializedName("tag") val tag: String,
    @SerializedName("name") val name: String,
    @SerializedName("badgeUrls") val badgeUrls: BadgeUrls,
    @SerializedName("clanLevel") val clanLevel: Int,
    @SerializedName("attacks") val attacks: Int,
    @SerializedName("stars") val stars: Int,
    @SerializedName("destructionPercentage") val destructionPercentage: Double,
    @SerializedName("members") val members: List<Member>
)
