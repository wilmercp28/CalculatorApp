package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
import java.text.DecimalFormat

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
fun MainScreen(functions: Functions = Functions()) {
    val buttonsSeparation = 10.dp
    val screenSize = 100.dp
    val buttonsSize = 200.dp
    val currentExpression = rememberSaveable { mutableStateOf("") }
    val pastExpression: MutableList<String> by rememberSaveable { mutableStateOf(mutableListOf()) }
    val df = DecimalFormat("#.##")
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(buttonsSeparation),
    ) {
        Spacer(modifier = Modifier.size(100.dp))
        Column {
            Text(
                text = pastExpression.joinToString("\n"),
                textAlign = TextAlign.End
            )
        }
        Text(
            text = currentExpression.value,
            textAlign = TextAlign.End

        )
        // Calculator Layout
        val buttonSymbols = listOf(
            "AC", "%","M-", "M+",
            "√", "^", "()", "/",
            "1", "2", "3", "*",
            "4", "5", "6", "-",
            "7", "8", "9", "+",
            "0", ".", "<", "="
        )
        val rows = buttonSymbols.chunked(4)
        for (rowSymbols in rows) {
            Row (
                modifier = Modifier,
            ) {
                for (symbol in rowSymbols) {
                   val buttonsColor = if (
                        symbol.isDigitsOnly()||symbol == "."||symbol == "<"
                    ){
                       MaterialTheme.colorScheme.primary
                    } else{
                        MaterialTheme.colorScheme.tertiary
                    }
                    KeyPadButtons(symbol,
                        buttonsColor,
                        currentExpression,
                        buttonsSize,
                        functions,
                        pastExpression,
                        df
                    )
                    Spacer(modifier = Modifier.size(buttonsSeparation))
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
    functions: Functions,
    pastExpression: MutableList<String>,
    df: DecimalFormat
){
    Box(
        modifier = Modifier
            .size(buttonsSize / 3)
            .background(backgroundColor, RoundedCornerShape(buttonsSize))
            .clickable {
                if (symbol in "1234567890.") {
                    calculatorScreenText.value += symbol
                }
                if (symbol in "%/*-+^√") {
                    functions.signsHandling(symbol, calculatorScreenText)
                }
                if (symbol == "<") {
                    functions.backSpace(calculatorScreenText)
                }
                if (symbol == "()") {
                    functions.parenthesis(calculatorScreenText)
                }
                if (symbol == "=") {
                    functions.equal(calculatorScreenText, pastExpression,df)
                }
                if (symbol == "AC") {
                    calculatorScreenText.value = ""
                }
            },
        contentAlignment = Center,
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