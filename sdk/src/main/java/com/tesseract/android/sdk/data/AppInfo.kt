package com.tesseract.android.sdk.data

import android.graphics.drawable.Drawable

data class AppInfo(var appName: String = "",
                   var packageName: String = "",
                   var icon: Drawable? = null,
                   var activityName: String = "",
                   var isPreInstalledApp: Boolean = false,
                   var version: Version? = null)