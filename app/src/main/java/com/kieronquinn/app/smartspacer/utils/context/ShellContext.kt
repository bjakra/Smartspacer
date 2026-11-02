package com.kieronquinn.app.smartspacer.utils.context

import android.annotation.SuppressLint
import android.content.AttributionSource
import android.content.Context
import android.content.ContextWrapper
import com.kieronquinn.app.smartspacer.service.SmartspacerShizukuService.Companion.ROOT_PACKAGE
import com.kieronquinn.app.smartspacer.service.SmartspacerShizukuService.Companion.ROOT_UID
import com.kieronquinn.app.smartspacer.service.SmartspacerShizukuService.Companion.SHELL_PACKAGE
import com.kieronquinn.app.smartspacer.service.SmartspacerShizukuService.Companion.SHELL_UID

class ShellContext(context: Context, private val isRoot: Boolean) : ContextWrapper(context) {

    // Graphite: the privileged service now runs as the app, so this reports the app itself
}