package com.kieronquinn.app.smartspacer.utils.extensions

import android.util.Log

// Graphite: no Firebase, errors only go to the local log
fun logNonFatal(exception: Exception) {
    Log.w("Smartspacer", "Non-fatal error", exception)
}
