package com.katielonsdale.chatterbox.utils

import com.katielonsdale.chatterbox.SessionManager
import java.time.OffsetDateTime

object TouAcceptanceValidator {
    fun validate(
        lastTouAcceptance: String?
    ) {
        if (!lastTouAcceptance.isNullOrEmpty()) {
            val lastTouAcceptanceDate = OffsetDateTime.parse(lastTouAcceptance)
            if (!SessionManager.latestTouDate.isAfter(lastTouAcceptanceDate)) {
                SessionManager.setIsTouUpToDate(true)
            }
        }
    }
}