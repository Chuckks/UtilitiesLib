package com.bbva.utilitieslib.extensions

import android.content.Context

fun Context.getAssetToStream(assetFile: String) = this.assets.open(assetFile)

fun Context.getAssetToBufferedReader(assetFile: String) = getAssetToStream(assetFile).bufferedReader().use {  }
fun Context.getAssetToByteArray(assetFile: String) = getAssetToStream(assetFile).use { it.readBytes() }
fun Context.getAssetToString(assetsFile: String) = String(getAssetToByteArray(assetsFile))