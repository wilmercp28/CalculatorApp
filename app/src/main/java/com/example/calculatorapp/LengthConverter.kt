package com.example.calculatorapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun MainLengthConverterScreen(


) {
    val lengthTextField = remember{ mutableStateOf("") }
    val buttonsSeparation = 5.dp
    val buttonsSize = 250.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(5.dp)
        ) {
            Text(text = "Input")
            Text(text = "Ouput")
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(5.dp)
        ) { 
            LengthKeyPad(
                lengthTextField,
                buttonsSeparation,
                buttonsSize
            )
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
                        lengthTextField
                    )

                }
            }
        }
    }
}