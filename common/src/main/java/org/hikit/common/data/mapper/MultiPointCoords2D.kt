package org.hikit.common.data.mapper

data class MultiPointCoords2D(
    var coordinates2D: List<List<Double>>
) {
    companion object {
        const val TYPE = "type"
        const val COORDINATES = "coordinates"
    }
}
