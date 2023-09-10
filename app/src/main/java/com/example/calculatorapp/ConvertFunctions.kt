package com.example.calculatorapp

import androidx.compose.runtime.MutableState
class ConvertFunctions {
    fun lengthConverter(
        input: MutableState<String>,
        inputUnit: MutableState<String>,
        outputUnit: MutableState<String>
    ): String {
        val inputValue = input.value.toDoubleOrNull() ?: 0.0
        val inputResult = when (inputUnit.value) {
            "Meter" -> inputValue
            "Centimeter" -> inputValue * 100
            "Millimeter" -> inputValue * 1000
            "Kilometer" -> inputValue / 1000
            "Mile" -> inputValue / 1609.34
            "Yard" -> inputValue * 1.09361
            "Foot" -> inputValue * 3.28084
            "Inch" -> inputValue * 39.3701
            "Nautical Mile" -> inputValue / 1852.0
            "Light Year" -> inputValue * 9.461e+15
            "Parsec" -> inputValue * 3.086e+16
            "Fathom" -> inputValue * 0.546807
            else -> inputValue
        }
        val result = when (outputUnit.value) {
            "Meter" -> inputResult
            "Centimeter" -> inputResult / 100
            "Millimeter" -> inputResult / 1000
            "Kilometer" -> inputResult * 1000
            "Mile" -> inputResult * 1609.34
            "Yard" -> inputResult / 1.09361
            "Foot" -> inputResult / 3.28084
            "Inch" -> inputResult / 39.3701
            "Nautical Mile" -> inputResult * 1852.0
            "Light Year" -> inputResult / 9.461e+15
            "Parsec" -> inputResult / 3.086e+16
            "Fathom" -> inputResult / 0.546807
            else -> inputResult

        }
        return result.toString()
    }
}