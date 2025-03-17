package org.hikit.common.dto

data class MatchingResponse (
    val results : List<TrailToScore>
)

data class TrailToScore(val trailData: Trail,
                        val accuracy: Int)

