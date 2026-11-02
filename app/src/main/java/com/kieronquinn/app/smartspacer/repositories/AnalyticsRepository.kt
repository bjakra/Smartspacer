package com.kieronquinn.app.smartspacer.repositories

import android.content.Context

// Graphite: no Firebase, so there is nothing to enable
interface AnalyticsRepository

class AnalyticsRepositoryImpl(
    private val context: Context,
    private val settingsRepository: SmartspacerSettingsRepository
): AnalyticsRepository
