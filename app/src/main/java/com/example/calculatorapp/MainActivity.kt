package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.ui.theme.CalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val calculatorScreenText = rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        val buttonsColor = MaterialTheme.colorScheme.primary
        TextField(
            value = calculatorScreenText.value,
            onValueChange = {newValue ->
                calculatorScreenText.value = newValue},
            maxLines = 2,
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 10.sp,
                textAlign = TextAlign.End
            )
        )
        Column {
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                KeyPadButtons("1", buttonsColor, calculatorScreenText)
                KeyPadButtons("2", buttonsColor, calculatorScreenText)
                KeyPadButtons("3", buttonsColor, calculatorScreenText)
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                KeyPadButtons("4", buttonsColor, calculatorScreenText)
                KeyPadButtons("5", buttonsColor, calculatorScreenText)
                KeyPadButtons("6", buttonsColor, calculatorScreenText)
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                KeyPadButtons("7", buttonsColor, calculatorScreenText)
                KeyPadButtons("8", buttonsColor, calculatorScreenText)
                KeyPadButtons("9", buttonsColor, calculatorScreenText)

            }
        }
    }
}

@Composable
fun KeyPadButtons(
    symbol: String,
    backgroundColor: Color,
    calculatorScreenText: MutableState<String>,
    ){
    val size = 50.dp
    Box(
        modifier = Modifier
            .size(size)
            .background(backgroundColor, RoundedCornerShape(size))
            .clickable {
                calculatorScreenText.value += symbol
            },
        contentAlignment = Center
    ) {
        Text(
            text = symbol,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun GreetingPreview() {
    CalculatorAppTheme {
        MainScreen()
    }
}