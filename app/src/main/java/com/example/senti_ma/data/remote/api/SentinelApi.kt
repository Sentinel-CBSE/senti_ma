package com.example.senti_ma.data.remote.api

import com.example.senti_ma.data.remote.dto.RobberyPointDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SentinelApi {

    @GET("api/robbery_points")
    suspend fun getRobberyPoints(
        @Query("northLat") northLat: Double,
        @Query("southLat") southLat: Double,
        @Query("eastLon") eastLon: Double,
        @Query("westLon") westLon: Double,
        @Query("from") fromTimestamp: Long,
        @Query("to") toTimestamp: Long,
        @Query("type") type: String?
    ): List<RobberyPointDto>

}
