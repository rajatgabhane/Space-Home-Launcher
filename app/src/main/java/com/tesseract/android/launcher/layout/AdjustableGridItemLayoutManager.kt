package com.tesseract.android.launcher.layout

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.math.max

class AdjustableGridItemLayoutManager(private val itemWidthPx: Int,
                                      context: Context) : GridLayoutManager(context, MIN_SPAN_COUNT) {

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        val totalSpace: Int = if (orientation == LinearLayoutManager.VERTICAL) {
            width - paddingRight - paddingLeft
        } else {
            height - paddingTop - paddingBottom
        }
        spanCount = max(MIN_SPAN_COUNT, totalSpace / itemWidthPx)
        super.onLayoutChildren(recycler, state)
    }

    companion object {
        private const val MIN_SPAN_COUNT = 1
    }
}