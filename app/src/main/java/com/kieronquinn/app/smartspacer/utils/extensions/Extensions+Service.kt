package com.kieronquinn.app.smartspacer.utils.extensions

import android.app.Notification
import android.app.Service
import android.content.pm.ServiceInfo
import android.os.Build
import com.kieronquinn.app.smartspacer.components.notifications.NotificationId

private const val GRAPHITE_NO_FOREGROUND = true

fun Service.startForeground(notificationId: NotificationId, notification: Notification) {
    // Graphite: nothing calls startForegroundService, and Android forces a foreground service's
    // notification to stay visible. As a battery-exempt system app it can run in the background.
    if(GRAPHITE_NO_FOREGROUND) return
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                notificationId.ordinal,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        }else{
            startForeground(notificationId.ordinal, notification)
        }
    }catch (e: Exception) {
        //Caches ForegroundServiceStartNotAllowedException on S+ when unable to startForeground
    }
}