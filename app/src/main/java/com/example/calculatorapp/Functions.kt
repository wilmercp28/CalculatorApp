package com.example.calculatorapp

import android.util.Log
import androidx.compose.runtime.MutableState
import java.util.Stack
import kotlin.math.pow
import kotlin.math.sqrt

class Functions {

    fun backSpace(
        text: MutableState<String>
    ) {
        if (!text.value.isEmpty()) {
            text.value = text.value.removeSuffix(text.value.last().toString())
        }
    }
    fun parenthesis(
        expression: MutableState<String>
    ){
        var openParenthesis = false
        for(char in expression.value) {
            if (char == '(') {
                openParenthesis = true
            }
            if (char == ')'){
                openParenthesis = false
            }
        }
        if (openParenthesis){
            expression.value += ")"
        } else{
            expression.value += "("
        }
    }
    //check if the last char is a symbol
    //avoid multiple signs
    fun signsHandling(
        symbol: String,
        expression: MutableState<String>
    ){
        val lastChar = expression.value.last().toString()
        val validSymbols = setOf("%", "/", "*", "-", "+", "^", "√")
        if (lastChar !in validSymbols){
            expression.value += symbol
        } else if (lastChar != symbol) {
            expression.value = expression.value.dropLast(1) + symbol
        }
    }

    fun equal(
        expression: MutableState<String>,
        pastScreen: MutableState<String>
    ) {
        evaluateExpression(expression)
        Log.d("Expression is ",expression.value)
    }

    private fun evaluateExpression(expression: MutableState<String>) {
        val operators = Stack<Char>()
        val numbers = Stack<Double>()
        val precedence = mapOf('+' to 1, '-' to 1, '*' to 2, '/' to 2, '^' to 3, '√' to 3)
        var currentNumber = ""
        for (char in expression.value) {
            if (char.isDigit() || char == '.') {
                currentNumber += char
            } else if (char == '(') {
                if (currentNumber.isNotEmpty()) {
                    numbers.push(currentNumber.toDouble())
                    currentNumber = ""
                    operators.push('*')
                }
                operators.push(char)
            } else if (char == ')') {
                while (operators.isNotEmpty() && operators.peek() != '(') {
                    val operator = operators.pop()
                    if (operator == '√') {
                        val operand = numbers.pop()
                        if (operand < 0) {
                            throw IllegalArgumentException("Cannot take square root of a negative number")
                        }
                        val result = sqrt(operand)
                        numbers.push(result)
                    } else {
                        val secondOperand = numbers.pop()
                        val firstOperand = numbers.pop()
                        val result = performOperation(operator, firstOperand, secondOperand)
                        numbers.push(result)
                    }
                }
                // Check the parenthesis
                if (operators.isNotEmpty() && operators.peek() == '(') {
                    operators.pop()
                }
            } else {
                if (currentNumber.isNotEmpty()) {
                    numbers.push(currentNumber.toDouble())
                    currentNumber = ""
                }
                while (operators.isNotEmpty() && precedence.getOrDefault(operators.peek(), 0) >= precedence.getOrDefault(char, 0)) {
                    val operator = operators.pop()
                    if (operator == '√') {
                        val operand = numbers.pop()
                        val result = Math.sqrt(operand)
                        numbers.push(result)
                    } else {
                        val secondOperand = numbers.pop()
                        val firstOperand = numbers.pop()
                        val result = performOperation(operator, firstOperand, secondOperand)
                        numbers.push(result)
                    }
                }
                operators.push(char)
            }
        }

        if (currentNumber.isNotEmpty()) {
            numbers.push(currentNumber.toDouble())
        }

        while (operators.isNotEmpty()) {
            val operator = operators.pop()
            if (operator == '√') {
                val operand = numbers.pop()
                val result = Math.sqrt(operand)
                numbers.push(result)
            } else {
                val secondOperand = numbers.pop()
                val firstOperand = numbers.pop()
                val result = performOperation(operator, firstOperand, secondOperand)
                numbers.push(result)
            }
        }
        expression.value = numbers.pop().toString()
    }
    private fun performOperation(operator: Char, firstOperand: Double, secondOperand: Double): Double {
        return when (operator) {
            '+' -> firstOperand + secondOperand
            '-' -> firstOperand - secondOperand
            '*' -> firstOperand * secondOperand
            '/' -> firstOperand / secondOperand
            '^' -> firstOperand.pow(secondOperand)
            else -> throw IllegalArgumentException("Unsupported operator: $operator")
        }
    }
}





