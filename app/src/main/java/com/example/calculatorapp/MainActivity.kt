package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
import java.text.DecimalFormat
import kotlin.math.PI

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
@Composable
fun MainScreen(functions: Functions = Functions()) {
    val buttonsSeparation = 5.dp
    val screenSize = 200.dp
    val buttonsSize = 200.dp
    val df = DecimalFormat("#.##")
    val fontSize = 20.sp
    val currentExpression = rememberSaveable { mutableStateOf("") }
    val results = rememberSaveable { mutableStateOf("") }
    val pastExpression: MutableList<String> by rememberSaveable { mutableStateOf(mutableListOf()) }
    Box(
        modifier = Modifier,
        contentAlignment = Center
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Calculator Layout
            val buttonSymbols = listOf(

                "AC", "π", "%", "√",
                "^", "(", ")", "/",
                "1", "2", "3", "*",
                "4", "5", "6", "-",
                "7", "8", "9", "+",
                "0", ".", "<", "="
            )
            val rows = buttonSymbols.chunked(4)
            for (rowSymbols in rows) {
                Row(
                    modifier = Modifier
                        .padding(buttonsSeparation),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(buttonsSeparation)
                ) {
                    for (symbol in rowSymbols) {
                        val buttonsColor = if (
                            symbol.isDigitsOnly() || symbol == "." || symbol == "<"
                        ) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.tertiary
                        }
                        KeyPadButtons(
                            symbol,
                            buttonsColor,
                            currentExpression,
                            buttonsSize,
                            functions,
                            pastExpression,
                            df,
                            results
                        )
                        Spacer(modifier = Modifier.size(buttonsSeparation))
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = TopCenter
    ) {
        CalculatorScreen(currentExpression, pastExpression, results)
    }
}

@Composable
fun CalculatorScreen(
    expression: MutableState<String>,
    pastExpression: MutableList<String>,
    results: MutableState<String>
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        var expanded by remember { mutableStateOf(false) }
        Text(
            text = expression.value,
            modifier = Modifier
                .padding(10.dp),
            fontSize = 30.sp,
        )
        Text(
            text = results.value,
            modifier = Modifier
                .padding(10.dp),
            fontSize = 20.sp
        )
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "History")
        }
        if (expanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                reverseLayout = true,
                content = {
                    itemsIndexed(pastExpression){index, item ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxSize(),
                            text = {
                                Text( pastExpression[index],
                                    textAlign = TextAlign.Center
                                )},
                            onClick = {
                                expression.value = item.substringAfter('=')
                                expanded = false
                            },


                        )
                    }
                }
            )
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
    df: DecimalFormat,
    result: MutableState<String>
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
                if (symbol == "(" || symbol == ")") {
                    functions.parenthesisHandling(calculatorScreenText, symbol)
                }
                if (symbol == "=") {
                    functions.equal(calculatorScreenText, pastExpression, df, result)
                }
                if (symbol == "AC") {
                    calculatorScreenText.value = ""
                }
                if (symbol == "π") {
                    calculatorScreenText.value += df.format(PI)
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