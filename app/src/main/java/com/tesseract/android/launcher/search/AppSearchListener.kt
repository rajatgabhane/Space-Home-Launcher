package com.tesseract.android.launcher.search

import androidx.appcompat.widget.SearchView
import com.tesseract.android.sdk.data.AppInfo
import com.tesseract.android.sdk.manager.AppDataManager

class AppSearchListener(private val filteredList: (List<AppInfo>) -> Boolean): SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String): Boolean =
        filteredList(AppDataManager.getFilteredAppsForQuery(newText))
}