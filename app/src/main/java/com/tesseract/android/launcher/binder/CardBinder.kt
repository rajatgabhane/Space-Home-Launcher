package com.tesseract.android.launcher.binder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.tesseract.android.launcher.R
import com.tesseract.android.launcher.adapter.CardViewHolder
import com.tesseract.android.launcher.util.showToast
import com.tesseract.android.sdk.data.AppInfo

class CardBinder {

    fun bind(card: CardViewHolder, data: AppInfo) {
        bindLabel(card.label, data)
        bindIcon(card.icon, data)
        bindItem(card.itemView, data)
    }

    private fun bindLabel(label: TextView, data: AppInfo) {
        label.text = data.appName
    }

    private fun bindIcon(icon: ImageView, data: AppInfo) {
        icon.setImageDrawable(data.icon)
    }

    private fun bindItem(item: View, data: AppInfo) {
        item.setOnClickListener { it.context.launchApp(data.packageName) }
        item.setOnLongClickListener { it.injectPopupMenu(data) }
    }

    private fun View.injectPopupMenu(data: AppInfo): Boolean =
        with(PopupMenu(context, this)) {
            inflate(R.menu.app_menu_options)
            setOnMenuItemClickListener { it.itemId.performMenuClickActions(context, data) }
            show()
        }.let { true }


    private fun Int.performMenuClickActions(context: Context, data: AppInfo) =
        when (this) {
            R.id.launch -> {
                context.launchApp(data.packageName).let { true }
            }
            R.id.uninstall -> {
                with(context) {
                    if (data.isPreInstalledApp) {
                        showToast(R.string.pre_installed_app_error_msg)
                    } else {
                        uninstallApp(data.packageName)
                    }
                }.let { true }
            }
            else -> false
        }

    private fun Context.launchApp(packageName: String) {
        val launchIntent: Intent? = this.packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.let { this.startActivity(it) }
    }

    private fun Context.uninstallApp(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        this.startActivity(intent)
    }
}