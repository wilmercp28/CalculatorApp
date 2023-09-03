package com.example.calculatorapp

import android.content.Context
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(functions: Functions = Functions()) {
    val isSettingsVisible = rememberSaveable{ mutableStateOf(false) }
    val context = LocalContext.current
    val saveData = SaveData()
    val buttonsSeparation = 5.dp
    val buttonsSize = 200.dp
    val df = DecimalFormat("#.##")
    val currentExpression = rememberSaveable { mutableStateOf("") }
    val results = rememberSaveable { mutableStateOf("") }
    var pastExpression by remember { mutableStateOf(saveData.loadListFromFile("Past_Expression_History", context)) }
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
                            results,
                            context
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
        CalculatorScreen(currentExpression, pastExpression, results,context)
    }
    Box(
        modifier = Modifier,
        contentAlignment = TopStart)
    {
        IconButton(
            onClick = {
isSettingsVisible.value = !isSettingsVisible.value
            }
        ) {
            Icon(imageVector = Icons.Default.Settings,"Settings")
        }
        SettingsMenu(isSettingsVisible)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenu(
    isSettingsVisible: MutableState<Boolean>
)
{
    if (isSettingsVisible.value) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp)),
            verticalArrangement = Arrangement.Center

        ) {
            Text(text = "Settings")
            Text(text = "Settings")
            Text(text = "Settings")
            Text(text = "Settings")
            Text(text = "Settings")
            Text(text = "Settings")
            Text(text = "Settings")

        }
    }

}



    @Composable
fun CalculatorScreen(
    expression: MutableState<String>,
    pastExpression: MutableList<String>,
    results: MutableState<String>,
    context: Context
){
    val saveData = SaveData()
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
                    item {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    pastExpression.clear()
                                    saveData.saveListToFile("Past_Expression_History",pastExpression, context)
                                    expanded = !expanded
                                },
                            text = "Clear",)
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
    result: MutableState<String>,
    context: Context
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
                    functions.equal(calculatorScreenText, pastExpression, df, result, context)
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