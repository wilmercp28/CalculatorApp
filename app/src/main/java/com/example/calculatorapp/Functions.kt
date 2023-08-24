package com.example.calculatorapp

import android.util.Log
import androidx.compose.runtime.MutableState

class Functions {

    fun backSpace(
        text: MutableState<String>
    ) {
        if (!text.value.isEmpty()) {
            text.value = text.value.removeSuffix(text.value.last().toString())
        }
    }

    fun equal(
        text: MutableState<String>
    ) {
        if (text.value.contains("+")){
            evaluateExpression(text, "+")
        }
        Log.d("Expression is ",text.value)
    }

    private fun evaluateExpression(expression: MutableState<String>, operator: String) {

        val input = expression.value
        val validOperators = listOf('+','-')
        val regexPatter = "[$validOperators]".toRegex()
        val match = regexPatter.find(input)
        if (match != null) {
            val operator = match.range.start
            val actualOperator = input.substring(0,operator)
            val operator1 = input[operator]
            val remainer = input.substring(operator + 1)
            Log.d("eeeee",actualOperator)
            Log.d("eeeeee",operator1.toString())
            Log.d("eeeeeee",remainer)
        }




        }

    private fun operation(
        expression: MutableState<String>,
        operator: String,
    ) {


    }
}





