package com.tesseract.android.launcher.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class CardViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    var label: TextView = item.label
    var icon: ImageView = item.icon
}