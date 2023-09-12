package com.example.calculatorapp

import android.util.Log
import androidx.compose.runtime.MutableState
import java.text.DecimalFormat
import kotlin.math.roundToInt

class ConvertFunctions {
    fun convert(
        input: MutableState<String>,
        inputUnit: MutableState<String>,
        outputUnit: MutableState<String>,
        roundingMode: Boolean,
        unit: String
    ): String {
        val maxDigitFormat = DecimalFormat("#.###############")
        val format = DecimalFormat("#.#")
        val results = when (unit) {
            "Length" -> length(input, inputUnit, outputUnit)
            "Volume" -> volume(input,inputUnit,outputUnit)
            else -> {
                0
            }
        }
        return if (results.toString() == "0.0") {
            ""
        } else {
            maxDigitFormat.format(results)
        }
    }


    private fun length(
        input: MutableState<String>,
        inputUnit: MutableState<String>,
        outputUnit: MutableState<String>
    ): Double {
        val inputValue = input.value.toDoubleOrNull() ?: 0.0
        val inputResult = when (inputUnit.value) {
            "Meter" -> inputValue
            "Centimeter" -> inputValue / 100
            "Millimeter" -> inputValue / 1000
            "Kilometer" -> inputValue * 1000
            "Mile" -> inputValue * 1609.34
            "Yard" -> inputValue / 1.09361
            "Foot" -> inputValue / 3.28084
            "Inch" -> inputValue * 39.3701
            "Nautical Mile" -> inputValue * 1852.0
            "Light Year" -> inputValue * 9.461e+15
            "Parsec" -> inputValue * 3.086e+16
            "Fathom" -> inputValue * 0.546807
            else -> inputValue
        }
        Log.d("Input", inputResult.toString())
        val result = when (outputUnit.value) {
            "Meter" -> inputResult
            "Centimeter" -> inputResult * 100
            "Millimeter" -> inputResult * 1000
            "Kilometer" -> inputResult / 1000
            "Mile" -> inputResult / 1609.34
            "Yard" -> inputResult * 1.09361
            "Foot" -> inputResult * 3.28084
            "Inch" -> inputResult * 39.3701
            "Nautical Mile" -> inputResult / 1852.0
            "Light Year" -> inputResult / 9.461e+15
            "Parsec" -> inputResult / 3.086e+16
            "Fathom" -> inputResult / 0.546807
            else -> inputResult

        }
        return result
    }

    private fun volume(
        input: MutableState<String>,
        inputUnit: MutableState<String>,
        outputUnit: MutableState<String>
    ): Double {
        Log.d("InputUnit", inputUnit.value)
        val inputValue = input.value.toDoubleOrNull() ?: 0.0
        val inputResult = when (inputUnit.value) {
            "Liter" -> inputValue
            "Milliliter" -> inputValue / 1000
            "Cubic Meter" -> inputValue * 1000
            "Cubic Foot" -> inputValue * 28.317
            "Cubic Inch" -> inputValue * 61.024
            "Gallon" -> inputValue * 3.785
            "Quart" -> inputValue / 1.057
            "Pint" -> inputValue / 2.113
            "Cup" -> inputValue / 4.167
            "Ounce" -> inputValue / 33.814
            "TableSpoon" -> inputValue / 67.628
            "TeaSpoon" -> inputValue / 202.9
            else -> inputValue
        }
        Log.d("Input", inputResult.toString())
        val result = when (outputUnit.value) {
            "Liter" -> inputResult
            "Milliliter" -> inputValue * 1000
            "Cubic Meter" -> inputValue / 1000
            "Cubic Foot" -> inputValue / 28.317
            "Cubic Inch" -> inputValue / 61.024
            "Gallon" -> inputValue / 3.785
            "Quart" -> inputValue * 1.057
            "Pint" -> inputValue * 2.113
            "Cup" -> inputValue * 4.167
            "Ounce" -> inputValue * 33.814
            "TableSpoon" -> inputValue * 67.628
            "TeaSpoon" -> inputValue * 202.9
            else -> inputResult

        }
        return result
    }
}