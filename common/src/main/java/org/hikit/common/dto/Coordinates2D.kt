package org.hikit.common.dto

import com.fasterxml.jackson.annotation.JsonCreator

class Coordinates2D(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) {
    @JsonCreator
    constructor(coordinates: Array<Double>) : this() {
        longitude = coordinates[0]
        latitude = coordinates[1]
    }
}

