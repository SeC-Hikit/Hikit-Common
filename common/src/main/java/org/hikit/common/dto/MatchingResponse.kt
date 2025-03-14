package org.hikit.common.dto

data class MatchingResponse (
    val results : Set<TrailToScore>
)

data class TrailToScore(val trailData: Trail,
                        val accuracy: Int)

