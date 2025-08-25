package com.example.freemanstats.api

import com.example.freemanstats.domain.model.ClanWar
import dagger.Provides
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ClashOfClansApi {
    @GET("clans/{clanTag}/currentwar")
    suspend fun getClanWar(
        @Path("clanTag") clanTag: String
    ): Response<ClanWar>
}