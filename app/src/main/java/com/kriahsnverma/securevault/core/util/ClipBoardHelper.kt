package com.kriahsnverma.securevault.core.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

fun copyToClipboard(context: Context, label:String, value: String ){
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, value))

    Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
}