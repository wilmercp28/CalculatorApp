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
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import java.security.KeyStore.TrustedCertificateEntry

@Composable
fun MainLengthConverterScreen(


) {
    val swapIcon: Painter = painterResource(id = R.drawable.baseline_swap_horiz_24)
    val context = LocalContext.current
    val roundNumber = remember { mutableStateOf(SaveData(context).getSettingsData("lengthRoundingMode","True").toBoolean()) }
    val lengthTextFieldInput = remember{ mutableStateOf("") }
    val outputUnitLength = remember{ mutableStateOf(SaveData(context).getSettingsData("outputUnitLength","Meter")) }
    val inputUnitLength = remember{ mutableStateOf(SaveData(context).getSettingsData("inputUnitLength","Centimeter")) }
    val lengthUnits = listOf(
        "Meter",
        "Centimeter",
        "Millimeter",
        "Kilometer",
        "Mile",
        "Yard",
        "Foot",
        "Inch",
        "Nautical Mile",
        "Light Year",
        "Parsec",
        "Fathom",
        // Add more length units as needed
    )
    val buttonsSeparation = 5.dp
    val buttonsSize = 250.dp
    val lengthTextFieldOutput = remember{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = "Rounding Mode")
            Switch(
                checked = roundNumber.value ,
                onCheckedChange = {
                    roundNumber.value = it
                    SaveData(context).saveSettingsData("lengthRoundingMode",roundNumber.value.toString())
                    lengthTextFieldOutput.value = ConvertFunctions().lengthConverter(
                        lengthTextFieldInput,
                        inputUnitLength,
                        outputUnitLength,
                        roundNumber.value
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
            LengthConvertorInputOutputText(lengthTextFieldInput,inputUnitLength,lengthUnits,context,true)
            Icon(
                painter = swapIcon,
                contentDescription = "SwapIcon",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    val unit1 = inputUnitLength.value
                    val unit2 = outputUnitLength.value
                    inputUnitLength.value = unit2
                    outputUnitLength.value = unit1
                    lengthTextFieldOutput.value = ConvertFunctions().lengthConverter(
                        lengthTextFieldInput,
                        inputUnitLength,
                        outputUnitLength,
                        roundNumber.value
                    )
                }
            )
            LengthConvertorInputOutputText(lengthTextFieldOutput,outputUnitLength,lengthUnits,context,false)
        }
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
                .clickable {
                    lengthTextFieldOutput.value = ConvertFunctions().lengthConverter(
                        lengthTextFieldInput,
                        inputUnitLength,
                        outputUnitLength,
                        roundNumber.value
                    )
                },
            contentAlignment = Center
        ){
            Text(text = "Convert")
        }
        Box(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(5.dp)
        ) {
            LengthKeyPad(
                lengthTextFieldInput,
                buttonsSeparation,
                buttonsSize
            )
        }
    }
}

@Composable
fun LengthConvertorInputOutputText(
    text: MutableState<String>,
    unit: MutableState<String>,
    lengthUnits: List<String>,
    context: Context,
    isInput: Boolean
) {

    val expanded = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .width(170.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
                .padding(5.dp),
            horizontalAlignment = CenterHorizontally,
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
                            SaveData(context).saveSettingsData("inputUnitLength",unit.value)
                        } else {
                            SaveData(context).saveSettingsData("outputUnitLength",unit.value)
                        }
                        expanded.value = false
                    }
                )
            }
            }
        }
    }


@Composable
private fun LengthKeyPad(
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