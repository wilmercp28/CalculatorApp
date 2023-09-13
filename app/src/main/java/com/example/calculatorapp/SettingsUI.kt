package com.example.calculatorapp

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsMenu(
    isSettingsVisible: MutableState<Boolean>,
    selectedDecimal: MutableState<String>,
    context: Context

)
{
    val settings: MutableList<String> = mutableListOf()
    if (isSettingsVisible.value) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(20.dp)),
            verticalArrangement = Arrangement.Center

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(text = "Decimal Places")
                Spacer(modifier = Modifier.weight(1f))
                DecimalPlacesButton(selectedDecimal,"0",context)
                DecimalPlacesButton(selectedDecimal,"1",context)
                DecimalPlacesButton(selectedDecimal,"2",context)
                DecimalPlacesButton(selectedDecimal,"3",context)
                DecimalPlacesButton(selectedDecimal,"4",context)
            }
        }
    }

}