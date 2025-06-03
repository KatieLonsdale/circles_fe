package com.katielonsdale.chatterbox.utils

import com.katielonsdale.chatterbox.SessionManager
import java.time.OffsetDateTime

object TouAcceptanceValidator {
    fun validate(
        lastTouAcceptance: String?
    ) {
        val lastTouAcceptanceDate = OffsetDateTime.parse(lastTouAcceptance)
        if (lastTouAcceptance == null || SessionManager.latestTouDate.isAfter(lastTouAcceptanceDate)) {
            SessionManager.setIsTouUpToDate(false)
        } else {
            SessionManager.setIsTouUpToDate(true)
        }
    }
}