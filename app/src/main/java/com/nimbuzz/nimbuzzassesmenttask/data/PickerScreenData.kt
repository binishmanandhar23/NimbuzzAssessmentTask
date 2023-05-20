package com.nimbuzz.nimbuzzassesmenttask.data

import android.net.Uri

data class PickerScreenData(val title: String, val buttonName: String, val previewUri: Uri? = null, val onChoose:(() -> Unit)? = null)
