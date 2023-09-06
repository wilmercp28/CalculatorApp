package com.example.calculatorapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun MainScreenGraphing(

) {
    var steps = remember{ mutableStateOf("") }
    var xMin = remember { mutableStateOf("0") }
    var xMax = remember { mutableStateOf("") }
    var yMin = remember { mutableStateOf("0") }
    var yMax = remember { mutableStateOf("") }
    val buttonsSeparation = 5.dp
    val buttonsSize = 200.dp
    val selectedField = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        InputFieldsGraph(selectedField,steps,xMax,xMin,yMax,yMin)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Calculator Layout
            val buttonSymbols = listOf(

                "1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "0", ".", "<",
            )
            val rows = buttonSymbols.chunked(3)
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
                        KeyPadButtonsOnlyNumbers(
                            symbol,
                            buttonsColor,
                            buttonsSize,
                            selectedField,
                            steps,
                            xMax,
                            xMin,
                            yMax,
                            yMin
                            )
                        Spacer(modifier = Modifier.size(buttonsSeparation))
                    }
                }
            }
        }
    }
}
@Composable
fun InputFieldsGraph(
    selectedField: MutableState<String>,
    steps: MutableState<String>,
    xMax: MutableState<String>,
    xMin: MutableState<String>,
    yMax: MutableState<String>,
    yMin: MutableState<String>
) {
    Column(
        modifier = Modifier
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
       TextFieldGraph(steps,"Steps",Alignment.Center,selectedField)
        Row {
            TextFieldGraph(xMax,"xMax", Alignment.Center, selectedField)
            TextFieldGraph(xMin,"xMin", Alignment.Center, selectedField)
            TextFieldGraph(yMax,"yMax", Alignment.Center, selectedField)
            TextFieldGraph(yMin,"yMin", Alignment.Center, selectedField)
        }
    }
}

@Composable
fun TextFieldGraph(
    text: MutableState<String>,
    textFieldName: String,
    alignment: Alignment,
    selectedField: MutableState<String>
){
    val borderColor = if (selectedField.value == textFieldName){
        MaterialTheme.colorScheme.onBackground
    } else {
        Color.Transparent
    }
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(100.dp)
            .border(1.dp, borderColor, RoundedCornerShape(5.dp))
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(5.dp)
            )
            .clickable {
                selectedField.value = textFieldName
                Log.d("SelectedField", selectedField.value)
            },
        contentAlignment = alignment
    )
    {
        Text(
            text = text.value,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer),
            maxLines = 1
        )
    }

}

@Composable
fun KeyPadButtonsOnlyNumbers(
    symbol: String,
    backgroundColor: Color,
    buttonsSize: Dp,
    selectedField: MutableState<String>,
    steps: MutableState<String>,
    xMax: MutableState<String>,
    xMin: MutableState<String>,
    yMax: MutableState<String>,
    yMin: MutableState<String>
){
    Box(
        modifier = Modifier
            .size(buttonsSize / 3)
            .background(backgroundColor, RoundedCornerShape(buttonsSize))
            .clickable {
                when (selectedField.value) {
                    "Steps" -> steps.value += symbol
                    "xMax" -> xMax.value += symbol
                    "xMin" -> xMin.value += symbol
                    "yMax" -> yMax.value += symbol
                    "yMin" -> yMin.value += symbol
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

