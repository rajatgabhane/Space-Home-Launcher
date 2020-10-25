package com.tesseract.android.launcher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.android.launcher.R
import com.tesseract.android.launcher.binder.CardBinder
import com.tesseract.android.sdk.data.AppInfo


class CardAdapter(private var apps: ArrayList<AppInfo>) : RecyclerView.Adapter<CardViewHolder>() {
    private var binder = CardBinder()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false))

    override fun getItemCount(): Int = apps.size

    override fun onBindViewHolder(vh: CardViewHolder, position: Int) = binder.bind(vh, apps[position])
}


