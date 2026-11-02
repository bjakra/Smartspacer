package com.kieronquinn.app.smartspacer

import android.content.Context
import com.kieronquinn.app.smartspacer.model.database.Action
import com.kieronquinn.app.smartspacer.model.database.Target
import com.kieronquinn.app.smartspacer.repositories.DatabaseRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID

/**
 *  Graphite: the first run has no setup wizard, so this sets up a default that looks like Google's
 *  At a Glance: the date with the weather and next alarm beside it, and upcoming calendar events.
 */
object GraphiteDefaults {

    private const val PREFS = "graphite_defaults"
    private const val KEY_APPLIED = "applied"

    fun applyOnce(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if(prefs.getBoolean(KEY_APPLIED, false)) return
        val database by inject<DatabaseRepository>(DatabaseRepository::class.java)
        val app = BuildConfig.APPLICATION_ID
        GlobalScope.launch {
            database.addTarget(Target(UUID.randomUUID().toString(), "$app.target.calendar", 0, app))
            listOf("graphiteweather", "alarm").forEachIndexed { index, name ->
                database.addAction(
                    Action(UUID.randomUUID().toString(), "$app.complication.$name", index, app)
                )
            }
            prefs.edit().putBoolean(KEY_APPLIED, true).apply()
        }
    }

}
