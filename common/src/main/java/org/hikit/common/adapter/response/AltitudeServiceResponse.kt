package org.hikit.common.adapter.response


data class AltitudeApiResponse(val results: List<AltitudeDataPoint> = listOf())
data class AltitudeDataPoint(val latitude: Double = 0.0, val longitude: Double = 0.0, val elevation: Double = 0.0)

data class AltitudeServiceRequest(val locations: List<AltitudeApiRequestPoint> = mutableListOf())
data class AltitudeApiRequestPoint(val latitude: Double, val longitude: Double)