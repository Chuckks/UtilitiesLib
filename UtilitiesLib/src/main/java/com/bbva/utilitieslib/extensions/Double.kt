package com.bbva.utilitieslib.extensions

import java.text.DecimalFormat

fun Double.formatAmount(symbol: String) = "$symbol " + DecimalFormat("#,###,##0.00").format(this)
