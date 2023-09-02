package com.example.calculatorapp

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Functions {
    fun backSpace(
        text: MutableState<String>
    ) {
        if (text.value.isNotEmpty()) {
            text.value = text.value.removeSuffix(text.value.last().toString())
        }
    }
    fun parenthesisHandling(
        expression: MutableState<String>,
        symbol: String
    ) {
        if (expression.value.isNotEmpty()) {
            if (symbol == ")") {
                expression.value += symbol
            } else {
                if (expression.value.last().isDigit() || expression.value.last() == ')') {
                    expression.value += "*$symbol"
                } else {
                    expression.value += symbol
                }
            }
        } else {
            expression.value += symbol
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
    private fun time(
    ): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd hh:mm a")
        return formatter.format(currentDateTime).toString()
    }
    fun equal(
        expression: MutableState<String>,
        pastExpression: MutableList<String>,
        df: DecimalFormat,
        result: MutableState<String>,
        context: Context
    ) {
        val saveData = SaveData()
        val pastOperation = expression.value
        if (expression.value.isNotEmpty()) {
            evaluateExpression(expression)
            try {
                val formattedResult = df.format(expression.value.toDouble()).toString()
                result.value = "= $formattedResult"
                val newEntry = "${time()}\n$pastOperation = $formattedResult"
                pastExpression.add(newEntry)
                saveData.saveListToFile("Past_Expression_History", pastExpression, context)
                expression.value = ""
            } catch (e: NumberFormatException){
                expression.value = ""
                result.value = "Invalid Number / Operation"
            }
        }
    }
    private fun evaluateExpression(
        expression: MutableState<String>
    ) {
            while (expression.value.contains('(') && expression.value.contains(')')) {
                parenthesis(expression)
            }
            if (expression.value.contains('^')) {
                exponentiation(expression)
            }else if (expression.value.contains('√')){
                squareRoot(expression)
            }else if(expression.value.contains('%')){
                percentage(expression)
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
        val match = pattern.find(updatedContent)
        if (match != null) {
            val contentInsideParentheses = mutableStateOf(match.groupValues[1])
            evaluateExpression(contentInsideParentheses).toString()
            updatedContent = updatedContent.replaceFirst(
                Regex.escapeReplacement(match.value),
                contentInsideParentheses.value
            )
            expression.value = updatedContent
            if (expression.value != "()") {
                evaluateExpression(expression)
            }
        } else {
            expression.value = "Invalid"
        }
    }
    private fun squareRoot(
        expression: MutableState<String>
    ){
        val pattern = Regex("√(-*\\d+(\\.\\d+)?)")
        val match = pattern.find(expression.value)
        if (match != null) {
            Log.d("Match", match.value)
            val operation = match.value
            val number = operation.removePrefix("√").toDouble()
            val result = sqrt(abs(number)).toString()
            val updatedExpression = expression.value.replaceFirst(operation, result)
            expression.value = updatedExpression
            evaluateExpression(expression)
        }
    }


    private fun percentage(
        expression: MutableState<String>
    ){
        val pattern = Regex("\\d+(?:\\.\\d+)?[+\\-*/]\\d+(?:\\.\\d+)?%")
        val match = pattern.find(expression.value)
        if (match != null) {
            var updatedExpression = expression.value
            val operation = match.value
            Log.d("operation", operation)
            val parts = operation.split("*")
            val number = parts[0].toDouble()
            val percentage = parts[1].removeSuffix("%").toDouble()
            val result = ((abs(number) * percentage) / 100).toString()
            updatedExpression = updatedExpression.replaceFirst(operation, result)
            expression.value = updatedExpression
            evaluateExpression(expression)
        }
    }
    private fun exponentiation(
        expression: MutableState<String>
    ) {
        val pattern = Regex("-*\\d+(?:\\.\\d+)?\\^-*\\d+(?:\\.\\d+)?")
        val match = pattern.find(expression.value)
        if (match != null) {
            var updatedExpression = expression.value
            val operation = match.value
            val parts = operation.split("^")
            val base = parts[0].toDouble()
            val exponent = parts[1].toDouble()
            val result = base.pow(exponent).toString()
            updatedExpression = updatedExpression.replaceFirst(operation, result)
            expression.value = updatedExpression
            evaluateExpression(expression)
        }
    }
    private fun multiplicationAndDivision(
        expression: MutableState<String>
    ) {
        val pattern = Regex("-*\\d+(?:\\.\\d+)?[*/]-*\\d+(?:\\.\\d+)?")
        val match = pattern.find(expression.value)
        if (match != null) {
            var updatedExpression = expression.value
            val operation = match.value
            Log.d("Match", operation)
            val parts = operation.split(Regex("[*/]"))
            Log.d("Parts", parts.toString())
            val leftOperand = parts[0].toDouble()
            val rightOperand = parts[1].toDouble()
            val result = when {
                operation.contains("*") -> (leftOperand * rightOperand).toString()
                rightOperand != 0.0 -> (leftOperand / rightOperand).toString()
                else -> "Error: Division by zero"
            }
            Log.d("Results", result)
            updatedExpression = updatedExpression.replaceFirst(operation, result)
            expression.value = updatedExpression
            evaluateExpression(expression)
        }
    }
    private fun additionAndSubtraction(
        expression: MutableState<String>
    ) {
        val pattern = Regex("-*\\d+(?:\\.\\d+)?[+\\-]\\d+(?:\\.\\d+)?")
        val match = pattern.find(expression.value)
        var updatedExpression = expression.value
        if (match != null) {
            val operation = match.value
            Log.d("Operation",operation)
            val parts = operation.split(Regex("[+-]")).toMutableList()
            if (parts.size == 3){
                parts[0] = "-${parts[0]}${parts[1]}"
                parts[1] = parts[2]
            }
            val leftOperand = parts[0].toDouble()
            val rightOperand = parts[1].toDouble()
            Log.d("Parts",parts.toString())
            val result = when {
                operation.contains("+") -> (leftOperand + rightOperand)
                else -> (leftOperand - rightOperand)
            }
            Log.d("Results", result.toString())
            updatedExpression = updatedExpression.replaceFirst(operation, result.toString())
            expression.value = updatedExpression
            evaluateExpression(expression)
        }
    }
}








