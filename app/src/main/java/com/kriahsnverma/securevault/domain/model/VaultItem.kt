package com.kriahsnverma.securevault.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class VaultItem(
    val id:Int,
    val title:String,
    val subtitle:String,
    val icon: ImageVector,
)
