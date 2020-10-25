package com.tesseract.android.launcher.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tesseract.android.launcher.receiver.AppInstallStatusReceiver
import com.tesseract.android.launcher.R
import com.tesseract.android.launcher.adapter.CardAdapter
import com.tesseract.android.launcher.layout.AdjustableGridItemLayoutManager
import com.tesseract.android.launcher.search.AppSearchListener
import com.tesseract.android.launcher.viewmodel.LauncherViewModel
import com.tesseract.android.sdk.data.AppInfo
import com.tesseract.android.sdk.manager.AppDataManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var appInstallStatusReceiver: AppInstallStatusReceiver? = null
    private var appList: ArrayList<AppInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = setupRecycler()
        setupViewModel(adapter)
        setupSearchBar(adapter)
        registerAppInstallationStatusReceiver()
    }

    private fun setupRecycler(): CardAdapter {
        val adapter = CardAdapter(appList)
        recycler_view.adapter = adapter
        val iconWidthPx = resources.getDimensionPixelSize(R.dimen.icon_size)
        val cardPaddingStartPx = resources.getDimensionPixelSize(R.dimen.card_padding)
        val cardPaddingEndPx = resources.getDimensionPixelSize(R.dimen.card_padding)
        val cardWidthPx = cardPaddingStartPx + iconWidthPx + cardPaddingEndPx
        val layoutManager = AdjustableGridItemLayoutManager(cardWidthPx, this)
        recycler_view.layoutManager = layoutManager
        return adapter
    }

    private fun setupViewModel(adapter: CardAdapter) {
        val viewModel = ViewModelProvider.NewInstanceFactory().create(LauncherViewModel::class.java)
        viewModel.setup(this)
        viewModel.apps.observe(this, Observer<List<AppInfo>> { it.updateAppListAndNotify(adapter) })
    }

    private fun setupSearchBar(adapter: CardAdapter) {
        search.setOnQueryTextListener(AppSearchListener { it.updateAppListAndNotify(adapter) })
    }

    private fun List<AppInfo>.updateAppListAndNotify(adapter: CardAdapter): Boolean {
        appList.clear()
        val added = appList.addAll(this)
        if (added) adapter.notifyDataSetChanged()
        return added
    }

    private fun registerAppInstallationStatusReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        intentFilter.addAction(Intent.ACTION_MY_PACKAGE_REPLACED)
        intentFilter.addDataScheme(PACKAGE)
        appInstallStatusReceiver = AppInstallStatusReceiver()
        applicationContext.registerReceiver(appInstallStatusReceiver, intentFilter)
    }

    override fun onBackPressed() {
        //Back Press should be disabled
    }

    override fun onDestroy() {
        appInstallStatusReceiver?.let {
            applicationContext.unregisterReceiver(
                appInstallStatusReceiver
            )
        }
        AppDataManager.clearData()
        super.onDestroy()
    }

    companion object {
        private const val PACKAGE = "package"
    }
}
