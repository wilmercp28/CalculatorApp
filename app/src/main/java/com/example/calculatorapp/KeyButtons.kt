package com.example.calculatorapp

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import kotlin.math.PI

@Composable
fun KeyPadButtonsOnlyNumbers(
    symbol: String,
    backgroundColor: Color,
    buttonsSize: Dp,
    textField: MutableState<String>,
    maxNumbers: Int
){
    Box(
        modifier = Modifier
            .size(buttonsSize / 3)
            .background(backgroundColor, RoundedCornerShape(5.dp))
            .clickable {
                if (textField.value.length < maxNumbers) {
                    if (symbol in "1234567890") {
                        textField.value += symbol
                    }
                    if (symbol == "." && !textField.value.contains('.')) {
                        textField.value += symbol
                    }
                }
                if (symbol == "<" && textField.value.isNotEmpty()) {
                    textField.value = textField.value.substringBeforeLast(textField.value.last())
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun KeyPadButtonsWithSigns(
    symbol: String,
    backgroundColor: Color,
    calculatorScreenText: MutableState<String>,
    buttonsSize: Dp,
    functions: Functions,
    pastExpression: MutableList<String>,
    df: DecimalFormat,
    result: MutableState<String>,
    context: Context
){
    Box(
        modifier = Modifier
            .size(buttonsSize / 3)
            .background(backgroundColor, RoundedCornerShape(5.dp))
            .clickable {
                if (symbol in "1234567890") {
                    calculatorScreenText.value += symbol
                }
                if (symbol == "." && !calculatorScreenText.value.contains('.')) {
                    calculatorScreenText.value += symbol
                }
                if (symbol in "%/*-+^√") {
                    functions.signsHandling(symbol, calculatorScreenText)
                }
                if (symbol == "<") {
                    functions.backSpace(calculatorScreenText)
                }
                if (symbol == "(" || symbol == ")") {
                    functions.parenthesisHandling(calculatorScreenText, symbol)
                }
                if (symbol == "=") {
                    functions.equal(calculatorScreenText, pastExpression, df, result, context)
                }
                if (symbol == "AC") {
                    calculatorScreenText.value = ""
                }
                if (symbol == "π") {
                    calculatorScreenText.value += df.format(PI)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}