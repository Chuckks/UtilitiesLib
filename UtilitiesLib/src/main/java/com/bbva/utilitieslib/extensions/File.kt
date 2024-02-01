package com.bbva.utilitieslib.extensions

import android.content.Context
import android.content.res.Resources
import androidx.annotation.RawRes
import java.io.File

private fun Context.getAbsolutePath() = this.getExternalFilesDir(null)?.absolutePath

fun Context.createFile(fileName: String): File {
    val file = File(getAbsolutePath() + fileName)

    if (!file.exists()) {
        file.createNewFile()
    }

    return file
}

fun getFileName(path: String) = path.split("/").last()
fun Context.readBytesFile(fileName: String) = File(getAbsolutePath() + fileName).readBytes()

fun Context.getAssetToStream(assetsFile: String) = this.assets.open(assetsFile)

fun Context.getAssetToBufferedReader(assetsFile: String) = getAssetToStream(assetsFile).bufferedReader().use {  }
fun Context.getAssetToByteArray(assetsFile: String) = getAssetToStream(assetsFile).use { it.readBytes() }
fun Context.getAssetToString(assetsFile: String) = String(getAssetToByteArray(assetsFile))


fun Resources.getFileToStream(@RawRes id: Int) = this.openRawResource(id)
fun Resources.getFileToBufferedReader(@RawRes id: Int) = getFileToStream(id).bufferedReader()
fun Resources.getFileToString(@RawRes id: Int) = getFileToBufferedReader(id).readText()
fun Resources.getFileToByteArray(@RawRes id: Int) = getFileToStream(id).readBytes()
