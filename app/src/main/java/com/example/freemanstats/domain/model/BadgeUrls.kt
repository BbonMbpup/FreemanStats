package com.example.freemanstats.domain.model

import com.google.gson.annotations.SerializedName

data class BadgeUrls(
    @SerializedName("small") val small: String,
    @SerializedName("large") val large: String,
    @SerializedName("medium") val medium: String
)