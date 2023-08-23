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
        val operatorList = listOf<String>("+","-","*","/")
        val rightSide = expression.value.substringAfterLast(operator)
        val leftSide = expression.value.substringAfter(operator)

        for (operators in operatorList){
            val operan1 = leftSide.substringAfterLast(operators)
        }


        Log.d("Right",rightSide)
        Log.d("left",leftSide)



        }

    private fun operation(
        expression: MutableState<String>,
        operator: String,
    ) {


    }
}





