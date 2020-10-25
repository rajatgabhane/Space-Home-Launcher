package com.tesseract.android.launcher.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tesseract.android.sdk.data.AppInfo
import com.tesseract.android.sdk.manager.AppDataManager
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class LauncherViewModel : ViewModel() {
    val apps = MutableLiveData<List<AppInfo>>()
    private var appListChangeSubscription: Subscription? = null

    init {
        appListChangeSubscription = AppDataManager.appListChangeObservable()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                apps.postValue(it)
            }
    }

    fun setup(context: Context) {
        apps.value = AppDataManager.getListOfApps(context)
    }

    override fun onCleared() {
        appListChangeSubscription?.unsubscribe()
    }
}