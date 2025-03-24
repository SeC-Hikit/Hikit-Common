package org.hikit.common.dto

import org.hikit.common.processor.Coordinates

data class MatchingRequest (
    val code: String,
    val coordinates: List<Coordinates>,
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

data class Geometry (
    val type : String,
    val coordinates: List<List<List<Double>>>
)
