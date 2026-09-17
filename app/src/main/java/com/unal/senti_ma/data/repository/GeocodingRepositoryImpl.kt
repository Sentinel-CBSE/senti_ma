package com.unal.senti_ma.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.repository.GeocodingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GeocodingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val geocoder: Geocoder
) : GeocodingRepository {

    override suspend fun getCoordinatesFromAddress(
        address: String
    ): AppResult<Coordinates> =
        suspendCancellableCoroutine { continuation ->

            geocoder.getFromLocationName(
                address,
                1,
                object : Geocoder.GeocodeListener {

                    override fun onGeocode(addresses: MutableList<Address>) {
                        val result = addresses.firstOrNull()

                        if (result == null) {
                            continuation.resume(
                                AppResult.Error(
                                    context.getString(
                                        R.string.error_geocoding_address_not_found
                                    )
                                )
                            )
                            return
                        }

                        continuation.resume(
                            AppResult.Success(
                                Coordinates(
                                    latitude = result.latitude,
                                    longitude = result.longitude
                                )
                            )
                        )
                    }

                    override fun onError(errorMessage: String?) {
                        continuation.resume(
                            AppResult.Error(
                                context.getString(
                                    R.string.error_geocoding_failed
                                )
                            )
                        )
                    }
                }
            )
        }

    override suspend fun getAddressFromCoordinates(
        coordinates: Coordinates
    ): AppResult<String> =
        suspendCancellableCoroutine { continuation ->

            geocoder.getFromLocation(
                coordinates.latitude,
                coordinates.longitude,
                1,
                object : Geocoder.GeocodeListener {

                    override fun onGeocode(addresses: MutableList<Address>) {
                        val result = addresses.firstOrNull()

                        if (result == null) {
                            continuation.resume(
                                AppResult.Error(
                                    context.getString(
                                        R.string.error_reverse_geocoding_address_not_found
                                    )
                                )
                            )
                            return
                        }

                        continuation.resume(
                            AppResult.Success(
                                result.getAddressLine(0)
                            )
                        )
                    }

                    override fun onError(errorMessage: String?) {
                        continuation.resume(
                            AppResult.Error(
                                context.getString(
                                    R.string.error_reverse_geocoding_failed
                                )
                            )
                        )
                    }
                }
            )
        }

}
