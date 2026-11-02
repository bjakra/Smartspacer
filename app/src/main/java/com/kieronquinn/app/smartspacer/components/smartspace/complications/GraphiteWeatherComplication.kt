package com.kieronquinn.app.smartspacer.components.smartspace.complications

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.kieronquinn.app.smartspacer.R
import com.kieronquinn.app.smartspacer.sdk.model.SmartspaceAction
import com.kieronquinn.app.smartspacer.sdk.model.uitemplatedata.Icon
import com.kieronquinn.app.smartspacer.sdk.model.uitemplatedata.Text
import com.kieronquinn.app.smartspacer.sdk.provider.SmartspacerComplicationProvider
import com.kieronquinn.app.smartspacer.sdk.utils.ComplicationTemplate
import android.graphics.drawable.Icon as AndroidIcon

/**
 *  Graphite: the current weather from Graphite Weather, next to the date like Google's At a Glance.
 *  Shows nothing while weather is off.
 */
class GraphiteWeatherComplication: SmartspacerComplicationProvider() {

    companion object {
        private val WEATHER_URI = Uri.parse("content://org.graphite.weather.data/current")
    }

    private val observer by lazy {
        object: ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                notifyChange(provideContext(), GraphiteWeatherComplication::class.java)
            }
        }
    }

    private var isObserving = false

    override fun getSmartspaceActions(smartspacerId: String): List<SmartspaceAction> {
        if(!isObserving) {
            isObserving = true
            provideContext().contentResolver.registerContentObserver(WEATHER_URI, false, observer)
        }
        val cursor = try {
            provideContext().contentResolver.query(WEATHER_URI, null, null, null, null)
        }catch (e: SecurityException){
            null
        } ?: return emptyList()
        return cursor.use {
            if(!it.moveToFirst()) return emptyList()
            val state = it.getInt(it.getColumnIndexOrThrow("state"))
            val temperature = it.getInt(it.getColumnIndexOrThrow("temperature"))
            val description = it.getString(it.getColumnIndexOrThrow("description"))
            listOf(ComplicationTemplate.Basic(
                "graphite_weather",
                Icon(AndroidIcon.createWithResource(provideContext(), iconFor(state)), description),
                Text("$temperature°"),
                null
            ).create())
        }
    }

    private fun iconFor(state: Int) = when(state) {
        1, 3 -> R.drawable.ic_graphite_weather_sun
        2, 4 -> R.drawable.ic_graphite_weather_moon
        5, 6, 9, 10 -> R.drawable.ic_graphite_weather_cloud
        21, 22 -> R.drawable.ic_graphite_weather_snow
        else -> R.drawable.ic_graphite_weather_rain
    }

    override fun getConfig(smartspacerId: String?): Config {
        return Config(
            resources.getString(R.string.complication_graphite_weather_label),
            resources.getString(R.string.complication_graphite_weather_description),
            AndroidIcon.createWithResource(provideContext(), R.drawable.ic_graphite_weather_cloud),
            allowAddingMoreThanOnce = false
        )
    }

}
