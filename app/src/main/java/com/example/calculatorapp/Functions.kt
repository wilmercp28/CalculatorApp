package com.example.calculatorapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.text.DecimalFormat

class Functions {

    fun backSpace(
        text: MutableState<String>
    ) {
        if (text.value.isNotEmpty()) {
            text.value = text.value.removeSuffix(text.value.last().toString())
        }
    }

    fun parenthesisHandling(
        expression: MutableState<String>
    ) {
        var openParenthesis = false
        for (char in expression.value) {
            if (char == '(') {
                openParenthesis = true
            }
            if (char == ')') {
                openParenthesis = false
            }
        }
        if (openParenthesis) {
            expression.value += ")"
        } else if (expression.value.last().isDigit()) {
            expression.value += "*("
        } else {
            expression.value += "("
        }
    }

    //check if the last char is a symbol
    //avoid multiple signs
    fun signsHandling(
        symbol: String,
        expression: MutableState<String>
    ) {
        val lastChar = expression.value.lastOrNull().toString()
        val validSymbols = setOf("%", "/", "*", "-", "+", "^", "√")
        if (lastChar !in validSymbols || symbol == "√" && lastChar != "√" || lastChar != "%") {
            expression.value += symbol
        } else if (lastChar != symbol) {
            expression.value = expression.value.dropLast(1) + symbol
        }

    }

    fun equal(
        expression: MutableState<String>,
        pastExpression: MutableList<String>,
        df: DecimalFormat
    ) {
        if (!expression.value.first().isDigit()) {
            val lastIndex = pastExpression.lastIndex
            expression.value = pastExpression[lastIndex] + expression.value
            evaluateExpression(expression)
        }
        evaluateExpression(expression)
        pastExpression += df.format(expression.value.toDouble()).toString()
        expression.value = ""
    }

    private fun evaluateExpression(
        expression: MutableState<String>
    ) {
        if (expression.value.contains('(')){
            parenthesis(expression)
        }
        if (expression.value.contains('*')) {
            multiplication(expression)
        }
        if (expression.value.contains('/')) {
            division(expression)
        }
    }

    private fun parenthesis(
        expression: MutableState<String>
    ) {
        val pattern = Regex("\\(([^()]+)\\)")

        var updatedInput = expression.value

        while (pattern.containsMatchIn(updatedInput)) {
            val parenthesisMatch = pattern.find(updatedInput)
            val parenthesisContent = parenthesisMatch?.groups?.get(1)?.value

            if (parenthesisContent != null) {
                val result = evaluateExpression(parenthesisContent)
                updatedInput = updatedInput.replaceFirst("($parenthesisContent)", result.toString())
            }
        }

        expression.value = updatedInput
    }

    private fun multiplication(
        expression: MutableState<String>
    ) {
        val pattern = Regex("\\d+(?:\\.\\d+)?\\*\\d+(?:\\.\\d+)?")

        val multiplicationMatch = pattern.find(expression.value)
        val multiplication = multiplicationMatch?.value

        if (multiplication != null) {
            val parts = multiplication.split('*')
            val result = (parts[0].toDouble() * parts[1].toDouble()).toString()
            val updatedInput = expression.value.replaceFirst(multiplication, result)
            expression.value = updatedInput
        }
        evaluateExpression(expression)
    }
    private fun division(
        expression: MutableState<String>
    ) {
    }
}








