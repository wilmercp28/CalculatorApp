package com.example.calculatorapp

import androidx.compose.runtime.MutableState

class Functions{

    fun backSpace(
        text: MutableState<String>
    ){
        if (!text.value.isEmpty()) {
            text.value = text.value.removeSuffix(text.value.last().toString())
        }
    }
    fun equal(
        text: MutableState<String>
    ){
           val result = evaluateExpression(text.value)
           text.value = result.toString()
       }
    }
    private fun evaluateExpression(expression: String): Double {
        val operators = arrayOf("+", "-", "*", "/")
        for (operator in operators) {
            if (expression.contains(operator)) {
                val parts = expression.split(operator)
                val firstPart = parts[0]
                val remainingExpression = parts.drop(1).joinToString(operator)
                return when (operator) {
                    "+" -> firstPart.trim().toDouble() + evaluateExpression(remainingExpression)
                    "-" -> firstPart.trim().toDouble() - evaluateExpression(remainingExpression)
                    "*" -> firstPart.trim().toDouble() * evaluateExpression(remainingExpression)
                    "/" -> firstPart.trim().toDouble() / evaluateExpression(remainingExpression)
                    else -> throw IllegalArgumentException("Unsupported operator")
                }
            }
        }
        return expression.trim().toDouble()
    }




