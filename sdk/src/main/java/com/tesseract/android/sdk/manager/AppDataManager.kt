package com.tesseract.android.sdk.manager

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import com.tesseract.android.sdk.data.AppInfo
import com.tesseract.android.sdk.data.Version
import rx.Observable
import rx.subjects.PublishSubject


object AppDataManager {
private var listOfApps: ArrayList<AppInfo> = ArrayList()
private val publisher : PublishSubject<List<AppInfo>> = PublishSubject.create()

    private fun loadListOfApps(context: Context) {
        val listOfAppInfo: ArrayList<AppInfo> = ArrayList()
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val activities = packageManager.queryIntentActivities(intent, 0)
        for (resolvedInfo in activities) {
            val packageName = resolvedInfo.activityInfo.packageName
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.applicationInfo.flags
            val app = AppInfo(
                appName = resolvedInfo.loadLabel(packageManager).toString(),
                packageName = packageName,
                icon = resolvedInfo.loadIcon(packageManager),
                isPreInstalledApp = (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                version = Version(
                    name = packageInfo.versionName,
                    code =  packageInfo.versionCode)
            )
            listOfAppInfo.add(app)
        }

        listOfAppInfo.sortedBy { it.appName }
            .also { listOfApps.clear() }
            .toCollection(listOfApps)
    }

    fun getListOfApps(context: Context, refresh: Boolean = false): ArrayList<AppInfo> {
        if (listOfApps.isEmpty() || refresh) {
            loadListOfApps(context)
        }
        return listOfApps
    }

    fun refreshListOfApps(context: Context) {
        val apps = getListOfApps(context, true)
        listOfApps = apps
        publisher.onNext(apps)
    }

    fun getFilteredAppsForQuery(query: String) =
        if (query.isEmpty()) listOfApps else listOfApps.filter { it.appName.contains(query, true) }

    fun appListChangeObservable(): Observable<List<AppInfo>> = publisher.asObservable()

    fun clearData() {
        listOfApps.clear()
    }
}