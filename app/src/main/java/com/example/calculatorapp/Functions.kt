package com.example.calculatorapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import kotlin.concurrent.thread
import kotlin.math.exp
import kotlin.math.pow

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
        if (expression.value.isNotEmpty()) {
            for (char in expression.value) {
                if (char == '(') {
                    openParenthesis = true
                }
                if (char == ')') {
                    openParenthesis = false
                }
            }
        }
        if (openParenthesis) {
            expression.value += ")"
        } else if (expression.value.isNotEmpty() && expression.value.last().isDigit()) {
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
        if (expression.value.isNotEmpty()) {
            if (!expression.value.first().isDigit() && expression.value.first() != '(') {
                val lastIndex = pastExpression.lastIndex
                expression.value = pastExpression[lastIndex] + expression.value
                evaluateExpression(expression)
            }
            evaluateExpression(expression)
            pastExpression += df.format(expression.value.toDouble()).toString()
            expression.value = ""
        }
    }
    private fun evaluateExpression(
        expression: MutableState<String>
    ) {
        while (expression.value.contains('(')) {
            parenthesis(expression)
        }
        if (expression.value.contains('^')){
            exponentiation(expression)
        } else if (expression.value.contains('/') || expression.value.contains('*')) {
            multiplicationAndDivision(expression)
        } else if (expression.value.contains('+') || expression.value.contains('-')) {
            additionAndSubtraction(expression)
        }
    }
    private fun parenthesis(
        expression: MutableState<String>
    ) {
        val pattern = Regex("\\(([^()]+)\\)")
        var updatedContent = expression.value
        while (pattern.containsMatchIn(updatedContent)) {
            val match = pattern.find(updatedContent)
            if (match != null) {
                val contentInsideParentheses = mutableStateOf(match.groupValues[1])
                evaluateExpression(contentInsideParentheses).toString()
                updatedContent = updatedContent.replaceFirst(
                    Regex.escapeReplacement(match.value),
                    contentInsideParentheses.value
                )
            }
        }
        expression.value = updatedContent
    }
    private fun exponentiation(
        expression: MutableState<String>
    ) {
        val pattern = Regex("\\d+(?:\\.\\d+)?\\^\\d+(?:\\.\\d+)?")
        val match = pattern.find(expression.value)
        var updatedExpression = expression.value
        val operation = match!!.value
        val parts = operation.split("^")
        val base = parts[0].toDouble()
        val exponent = parts[1].toDouble()
        val result = base.pow(exponent).toString()
        updatedExpression = updatedExpression.replaceFirst(operation, result)
        expression.value = updatedExpression
        evaluateExpression(expression)
    }
    private fun multiplicationAndDivision(
        expression: MutableState<String>
    ) {
        val pattern = Regex("\\d+(?:\\.\\d+)?[*/]\\d+(?:\\.\\d+)?")
        val match = pattern.find(expression.value)
        var updatedExpression = expression.value
        val operation = match!!.value
        val parts = operation.split(Regex("[*/]"))
        val leftOperand = parts[0].toDouble()
        val rightOperand = parts[1].toDouble()
        val result = when {
            operation.contains("*") -> (leftOperand * rightOperand).toString()
            rightOperand != 0.0 -> (leftOperand / rightOperand).toString()
            else -> "Error: Division by zero"
        }
        updatedExpression = updatedExpression.replaceFirst(operation, result)
        expression.value = updatedExpression
        evaluateExpression(expression)
    }
    private fun additionAndSubtraction(
        expression: MutableState<String>
    ) {
        val pattern = Regex("\\d+(?:\\.\\d+)?[+\\-]\\d+(?:\\.\\d+)?")
        val match = pattern.find(expression.value)
        var updatedExpression = expression.value
        val operation = match!!.value
        val parts = operation.split(Regex("[+-]"))
        val leftOperand = parts[0].toDouble()
        val rightOperand = parts[1].toDouble()
        val result = when {
            operation.contains("+") -> (leftOperand + rightOperand).toString()
            else -> (leftOperand - rightOperand).toString()
        }
        updatedExpression = updatedExpression.replaceFirst(operation, result)
        expression.value = updatedExpression
        evaluateExpression(expression)
    }
}








