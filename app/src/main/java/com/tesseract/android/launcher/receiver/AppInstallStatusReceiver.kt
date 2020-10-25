package com.tesseract.android.launcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tesseract.android.sdk.manager.AppDataManager

class AppInstallStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AppDataManager.refreshListOfApps(context)
    }
}