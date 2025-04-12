package com.example.freemanstats.api

import com.example.freemanstats.model.ClanWar
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ClashOfClansApi {
    @GET("clans/{clanTag}/currentwar")
    suspend fun getClanWar(
        @Path("clanTag") clanTag: String,
        @Header("Authorization") apiKey: String
    ): Response<ClanWar>
}