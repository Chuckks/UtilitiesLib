package com.bbva.utilitieslib.utils

import android.util.Log
import com.bbva.utilitieslib.interfaces.IEmpty
import java.math.BigDecimal

private const val DEFAULT_SYMBOL = "$"
private const val DEFAULT_NUMBER_DECIMAL = 2
private const val DEFAULT_THOUSAND_SEPARATOR = ','
private const val DEFAULT_DECIMAL_SEPARATOR = '.'
private const val DEFAULT_RIGHT_POSITION = true
private const val DEFAULT_LENGTH_THOUSAND = 3

data class FormatAmount(
        val symbol: String = DEFAULT_SYMBOL,
        val numberDecimal: Int = DEFAULT_NUMBER_DECIMAL,
        val thousandSeparator: Char = DEFAULT_THOUSAND_SEPARATOR,
        val decimalSeparator: Char = DEFAULT_DECIMAL_SEPARATOR,
        val rightPosition: Boolean = DEFAULT_RIGHT_POSITION
                       ): IEmpty {

    private var amountAux = StringBuilder()

    override fun isEmpty() = symbol == DEFAULT_SYMBOL && numberDecimal == DEFAULT_NUMBER_DECIMAL && rightPosition == DEFAULT_RIGHT_POSITION
                             && thousandSeparator == DEFAULT_THOUSAND_SEPARATOR && decimalSeparator == DEFAULT_DECIMAL_SEPARATOR

    fun format(value: BigDecimal):String {
        val amount = value.toString()
        Log.i("FormatAmount", "Input Amount [$value] -> to String [${amount}]")
        return format(amount)
    }

    fun format(value: String): String {
        clear()

        if (!rightPosition) {
            addSymbol()
            addSpace()
        }

        if (value.isEmpty())
            addZero()
        else {
            addThousandSeparator(value)
            addDecimalSeparator(value)
        }

        if (rightPosition) {
            addSpace()
            addSymbol()
        }

        return build()
    }

    private fun clear() = amountAux.clear()
    private fun build() = amountAux.toString()

    private fun addDecimalSeparator(amount: String) {
        val length = amount.length

        if (amount.isEmpty()) {
            Log.e("AmountConfig", "Empty Amount")
            return
        }

        if (length <= numberDecimal) {
            amountAux.append("0$decimalSeparator")
                    .append("0".repeat(numberDecimal - length))
                    .append(amount)
        }
        else {
            val calc = length - numberDecimal
            if (numberDecimal > 0) {
                amountAux.append(decimalSeparator)
                        .append(amount.substring(calc))
            }
        }
    }

    private fun addThousandSeparator(amount: String) {
        if (amount.isEmpty()) {
            Log.e("AmountConfig", "Empty Amount")
            return
        }

        val length = amount.length
        if (length > numberDecimal) {
            val calc = length - numberDecimal
            val thousandAmount = amount.substring(0, calc)

            if (thousandAmount.length < DEFAULT_LENGTH_THOUSAND)
                amountAux.append(thousandAmount)
            else {
                val rem = thousandAmount.length % DEFAULT_LENGTH_THOUSAND
                var acum = DEFAULT_LENGTH_THOUSAND

                thousandAmount.forEachIndexed { index, char ->
                    if(rem != 0 && rem == index )
                        amountAux.append(thousandSeparator)
                       if (rem + acum == index) {
                        amountAux.append(thousandSeparator)
                        acum += DEFAULT_LENGTH_THOUSAND
                    }
                        amountAux.append(char)
                }
            }

        }

    }

    private fun addSymbol() = amountAux.append(symbol)
    private fun addSpace() = amountAux.append(" ")
    private fun addZero() = amountAux.append("0")
}