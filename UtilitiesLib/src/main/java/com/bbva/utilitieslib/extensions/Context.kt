package com.bbva.utilitieslib.extensions

import android.content.Context
import android.widget.Toast

fun Context?.toast(text: String, duration: Int = Toast.LENGTH_SHORT) = this?.let { Toast.makeText(it, text, duration).show() }
