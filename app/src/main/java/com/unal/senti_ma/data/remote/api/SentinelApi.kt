package com.unal.senti_ma.data.remote.api

import com.unal.senti_ma.data.remote.dto.InstallationIdRequestDto
import com.unal.senti_ma.data.remote.dto.RobberyPointDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SentinelApi {

    @POST("api/robbery")
    suspend fun createRobberyReport(
        @Header("ngrok-skip-browser-warning") skipWarning: String = "true",
        @Query("type") type: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timestamp") timestamp: Long
    ): Response<Unit>

    @GET("api/robbery")
    suspend fun getRobberyPoints(
        @Header("ngrok-skip-browser-warning") skipWarning: String = "true",
        @Query("northLat") northLat: Double,
        @Query("southLat") southLat: Double,
        @Query("eastLon") eastLon: Double,
        @Query("westLon") westLon: Double,
        @Query("from") fromTimestamp: Long,
        @Query("to") toTimestamp: Long,
        @Query("type") type: String?
    ): Response<List<RobberyPointDto>>

    @POST("api/location")
    suspend fun sendLocation(
        @Header("ngrok-skip-browser-warning") skipWarning: String = "true",
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<Unit>

    @POST("api/devices/register")
    suspend fun registerDeviceToken(
        @Header("ngrok-skip-browser-warning") skipWarning: String = "true",
        @Body request: InstallationIdRequestDto
    ): Response<Unit>

}
