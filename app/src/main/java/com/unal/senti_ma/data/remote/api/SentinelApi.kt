package com.unal.senti_ma.data.remote.api

import com.unal.senti_ma.data.remote.dto.EmergencyContactRequestDto
import com.unal.senti_ma.data.remote.dto.InstallationIdRequestDto
import com.unal.senti_ma.data.remote.dto.RobberyPointDto
import com.unal.senti_ma.data.remote.dto.RobberyReportRequestDto
import com.unal.senti_ma.data.remote.dto.UserDto
import com.unal.senti_ma.data.remote.dto.UserUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SentinelApi {

    @POST("robbery/create")
    suspend fun createRobberyReport(
        @Body report: RobberyReportRequestDto
    ): Response<Unit>

    @GET("robbery/list")
    suspend fun getRobberyPoints(
        @Query("northLat") northLat: Double,
        @Query("southLat") southLat: Double,
        @Query("eastLon") eastLon: Double,
        @Query("westLon") westLon: Double,
        @Query("from") fromTimestamp: Long,
        @Query("to") toTimestamp: Long,
        @Query("type") type: String?
    ): Response<List<RobberyPointDto>>

    @POST("location/send")
    suspend fun sendLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<Unit>

    @POST("notification/registerInstallationId")
    suspend fun registerInstallationId(
        @Body installationId: InstallationIdRequestDto
    ): Response<Unit>

    @PUT("profile/data")
    suspend fun updateProfile(
        @Body userUpdate: UserUpdateDto
    ): Response<UserDto>

    @POST("profile/emergency-contacts")
    suspend fun addEmergencyContact(
        @Body contact: EmergencyContactRequestDto
    ): Response<UserDto>

    @PUT("profile/emergency-contacts/{uid}")
    suspend fun updateEmergencyContact(
        @Path("uid") uid: String,
        @Body contact: EmergencyContactRequestDto
    ): Response<UserDto>

    @DELETE("profile/emergency-contacts/{uid}")
    suspend fun deleteEmergencyContact(
        @Path("uid") uid: String
    ): Response<UserDto>

}
