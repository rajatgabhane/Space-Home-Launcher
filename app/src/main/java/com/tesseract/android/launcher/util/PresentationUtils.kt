package com.tesseract.android.launcher.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(msg: Int, vararg args : String) =
    Toast.makeText(this, this.getString(msg, args), Toast.LENGTH_LONG).show()