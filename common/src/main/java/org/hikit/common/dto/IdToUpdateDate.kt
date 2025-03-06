package org.hikit.common.dto

import java.time.LocalDateTime

data class IdToUpdateDate(
    var id: String,
    var lastUpdateDate: LocalDateTime
)