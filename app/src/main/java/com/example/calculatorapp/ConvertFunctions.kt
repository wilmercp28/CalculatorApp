package com.example.calculatorapp

import android.util.Log
import androidx.compose.runtime.MutableState
import java.text.DecimalFormat
import kotlin.math.roundToInt

class ConvertFunctions {
    fun lengthConverter(
        input: MutableState<String>,
        inputUnit: MutableState<String>,
        outputUnit: MutableState<String>,
        roundingMode: Boolean
    ): String {
        val maxDigitFormat = DecimalFormat("#.###############")
        val format = DecimalFormat("#.#")
        val inputValue = input.value.toDoubleOrNull() ?: 0.0
        val inputResult = when (inputUnit.value) {
            "Meter" -> inputValue
            "Centimeter" -> inputValue / 100
            "Millimeter" -> inputValue / 1000
            "Kilometer" -> inputValue * 1000
            "Mile" -> inputValue * 1609.34
            "Yard" -> inputValue / 1.09361
            "Foot" -> inputValue * 3.28084
            "Inch" -> inputValue * 39.3701
            "Nautical Mile" -> inputValue / 1852.0
            "Light Year" -> inputValue * 9.461e+15
            "Parsec" -> inputValue * 3.086e+16
            "Fathom" -> inputValue * 0.546807
            else -> inputValue
        }
        Log.d("Input",inputResult.toString())
        val result = when (outputUnit.value) {
            "Meter" -> inputResult
            "Centimeter" -> inputResult * 100
            "Millimeter" -> inputResult * 1000
            "Kilometer" -> inputResult / 1000
            "Mile" -> inputResult / 1609.34
            "Yard" -> inputResult * 1.09361
            "Foot" -> inputResult / 3.28084
            "Inch" -> inputResult / 39.3701
            "Nautical Mile" -> inputResult * 1852.0
            "Light Year" -> inputResult / 9.461e+15
            "Parsec" -> inputResult / 3.086e+16
            "Fathom" -> inputResult / 0.546807
            else -> inputResult

        }
        if (result.toString() == "0.0"){
            return ""
        } else if (roundingMode) {
            val decimalPlacesToRound = when {
                result < 1.0 -> 3
                result < 10.0 -> 2
                else -> 1
            }
            format.applyPattern("#.${"#".repeat(decimalPlacesToRound)}")
            return format.format(result)
        } else {
            return maxDigitFormat.format(result)
        }
    }
}