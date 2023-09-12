package com.example.calculatorapp

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun UnitConverter(
    unitList: List<String>,
    unitName: String
    ) {
    val defaultValue = when(unitName){
        "Length" -> "Meter"
        "Volume" -> "Liter"
        else -> {
            "Invalid"
        }
    }
    val swapIcon: Painter = painterResource(id = R.drawable.baseline_swap_horiz_24)
    val context = LocalContext.current
    val roundNumber = remember { mutableStateOf(SaveData(context).getSettingsData("$unitName RoundingMode","True").toBoolean()) }
    val textFieldInput = remember{ mutableStateOf("") }
    val outputUnit = remember{ mutableStateOf(SaveData(context).getSettingsData("$unitName output",defaultValue)) }
    val inputUnit = remember{ mutableStateOf(SaveData(context).getSettingsData("$unitName input",defaultValue)) }
    val buttonsSeparation = 5.dp
    val buttonsSize = 250.dp
    val textFieldOutput = remember{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Rounding Mode")
        Switch(
            checked = roundNumber.value ,
            onCheckedChange = {
                roundNumber.value = it
                SaveData(context).saveSettingsData("$unitName RoundingMode",roundNumber.value.toString())
                textFieldOutput.value = ConvertFunctions().convert(
                    textFieldInput,
                    inputUnit,
                    outputUnit,
                    roundNumber.value,
                    unitName
                )
            },
            modifier = Modifier
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ConvertorInputOutputText(textFieldInput,inputUnit,unitList,context,true,unitName)
            Icon(
                painter = swapIcon,
                contentDescription = "SwapIcon",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        val unit1 = inputUnit.value
                        val unit2 = outputUnit.value
                        inputUnit.value = unit2
                        outputUnit.value = unit1
                        textFieldOutput.value = ConvertFunctions().convert(
                            textFieldInput,
                            inputUnit,
                            outputUnit,
                            roundNumber.value,
                            "Length"
                        )
                    }
            )
            ConvertorInputOutputText(textFieldOutput,outputUnit,unitList,context,false,unitName)
        }
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
                .clickable {
                    textFieldOutput.value = ConvertFunctions().convert(
                        textFieldInput,
                        inputUnit,
                        outputUnit,
                        roundNumber.value,
                        unitName
                    )
                },
            contentAlignment = Alignment.Center
        ){
            Text(text = "Convert")
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp)
        ) {
            KeyPad(
                textFieldInput,
                buttonsSeparation,
                buttonsSize
            )
        }
    }
}

@Composable
fun ConvertorInputOutputText(
    text: MutableState<String>,
    unit: MutableState<String>,
    lengthUnits: List<String>,
    context: Context,
    isInput: Boolean,
    unitName: String
) {

    val expanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .width(170.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text.value,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = unit.value,
            modifier = Modifier
                .clickable {
                    expanded.value = !expanded.value
                },
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { }) {
            for (lengthUnit in lengthUnits) {
                DropdownMenuItem(
                    { Text(text = lengthUnit) },
                    onClick = {
                        unit.value = lengthUnit
                        if (isInput) {
                            SaveData(context).saveSettingsData("$unitName input",unit.value)
                        } else {
                            SaveData(context).saveSettingsData("$unitName output",unit.value)
                        }
                        expanded.value = false
                    }
                )
            }
        }
    }
}


@Composable
private fun KeyPad(
    lengthTextField: MutableState<String>,
    buttonsSeparation: Dp,
    buttonsSize: Dp
) {
    Column {
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
                        lengthTextField,
                        16
                    )

                }
            }
        }
    }
}