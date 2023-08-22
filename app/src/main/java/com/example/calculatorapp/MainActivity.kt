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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
fun MainScreen(funtions: Functions = Functions()) {
    val calculatorScreenText = rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        val buttonsColor = MaterialTheme.colorScheme.primary
        val buttonsSize = 200.dp
        TextField(
            value = calculatorScreenText.value,
            onValueChange = { newValue ->
                calculatorScreenText.value = newValue
            },
            maxLines = 2,
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 10.sp,
                textAlign = TextAlign.End
            )
        )
        // Calculator Layout
        val buttonSymbols = listOf(
            "AC", "()", "%", "/",
            "1", "2", "3", "X",
            "4", "5", "6", "-",
            "7", "8", "9", "+",
            "0", ".", "<", "="
        )
        val rows = buttonSymbols.chunked(4)
        for (rowSymbols in rows) {
            Row {
                for (symbol in rowSymbols) {
                    KeyPadButtons(symbol, buttonsColor, calculatorScreenText, buttonsSize,funtions)
                }
            }
        }
    }
}
@Composable
fun KeyPadButtons(
    symbol: String,
    backgroundColor: Color,
    calculatorScreenText: MutableState<String>,
    buttonsSize: Dp,
    functions: Functions
    ){
    Box(
        modifier = Modifier
            .size(buttonsSize / 3)
            .background(backgroundColor, RoundedCornerShape(buttonsSize))
            .clickable {
                if (symbol in "1234567890.%/X-+") {
                    calculatorScreenText.value += symbol
                }
                if (symbol == "<"){
                    functions.backSpace(calculatorScreenText)
                }
                if (symbol == "="){
                    functions.equal(calculatorScreenText)
                }
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