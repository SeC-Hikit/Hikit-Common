package org.hikit.common.dto

data class MatchingRequest (
    val code: String,
    val geometry: GeometryParent,
    val metadata: StatsTrailMetadata,
)

data class StatsTrailMetadata(
    val totalRise: Double,
    val totalFall: Double,
    val length: Double,
    val totalEta: Double,
    val highest: Double,
    val lowest: Double,
)

data class GeometryParent (
    val geoline : Geometry
)

data class Geometry (
    val type : String,
    val coordinates: List<List<Double>>
)