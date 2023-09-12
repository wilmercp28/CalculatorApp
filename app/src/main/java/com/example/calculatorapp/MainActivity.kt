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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    val selectedItem = remember { mutableStateOf(SaveData(context).getSettingsData("Screen","0")) }
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                Text("Calculators", modifier = Modifier.padding(16.dp))
                                Divider()
                                NavigationDrawerItem(
                                    label = { Text(text = "Calculator") },
                                    selected = selectedItem.value == "0",
                                    onClick = {
                                        selectedItem.value = "0"
                                        scope.launch { drawerState.close() }
                                        SaveData(context).saveSettingsData("Screen","0")
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text(text = "Graphing") },
                                    selected = selectedItem.value == "1",
                                    onClick = {
                                        selectedItem.value = "1"
                                        scope.launch { drawerState.close() }
                                        SaveData(context).saveSettingsData("Screen","1")
                                    }
                                )
                                Divider(thickness = 2.dp)
                                Text(text = "Converter", textAlign = TextAlign.Center)
                                Divider(thickness = 2.dp)
                                NavigationDrawerItem(
                                    label = { Text(text = "Length") },
                                    selected = selectedItem.value == "2",
                                    onClick = {
                                        selectedItem.value = "2"
                                        scope.launch { drawerState.close() }
                                        SaveData(context).saveSettingsData("Screen","2")
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text(text = "Volume") },
                                    selected = selectedItem.value == "3",
                                    onClick = {
                                        selectedItem.value = "3"
                                        scope.launch { drawerState.close() }
                                        SaveData(context).saveSettingsData("Screen","3")
                                    }
                                )
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Center
                        ) {

                            when (selectedItem.value){
                                "0" ->  MainScreen()
                                "1" -> MainScreenGraphing()
                                "2" -> UnitConverter(UnitLists().getLengthList(),"Length")
                                "3" -> UnitConverter(UnitLists().getVolumeList(),"Volume")
                            }

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen(functions: Functions = Functions()) {
    val context = LocalContext.current
    val saveData = SaveData(context)
    val selectedDecimal = remember { mutableStateOf(saveData.getSettingsData("DecimalPlace","0")) }
    val isSettingsVisible = rememberSaveable{ mutableStateOf(false) }
    val buttonsSeparation = 1.dp
    val buttonsSize = 250.dp
    val currentExpression = rememberSaveable { mutableStateOf("") }
    val results = rememberSaveable { mutableStateOf("") }
    var pastExpression by remember { mutableStateOf(saveData.loadListFromFile("Past_Expression_History", context)) }
    val decimalPatter = when (selectedDecimal.value) {
        "0" -> "#"
        "1" -> "#.#"
        "2" -> "#.##"
        "3" -> "#.###"
        "4" -> "#.####"
        else -> {"#"}
    }
    val df = DecimalFormat(decimalPatter)
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = BottomCenter
    )
    {
    Column(
        modifier = Modifier,
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
                    KeyPadButtonsWithSigns(
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
        CalculatorScreen(currentExpression,pastExpression, results,context)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = TopStart
    )
    {
        IconButton(
            onClick = {
                isSettingsVisible.value = !isSettingsVisible.value
            }
        ) {
            Icon(imageVector = Icons.Default.Settings,"Settings")
        }
        SettingsMenu(isSettingsVisible, selectedDecimal,context)
    }
}

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

@Composable
fun DecimalPlacesButton(
    selectedDecimal: MutableState<String>,
    decimalNumber: String,
    context: Context
){
    val saveData =  SaveData(context)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RadioButton(
            selected = selectedDecimal.value == decimalNumber,
            onClick = {
                selectedDecimal.value = decimalNumber
                saveData.saveSettingsData("DecimalPlace",decimalNumber)
                      },
            modifier = Modifier
                .size(30.dp)
        )
        Text(text = decimalNumber)
    }
}



@Composable
fun CalculatorScreen(
    expression: MutableState<String>,
    pastExpression: MutableList<String>,
    results: MutableState<String>,
    context: Context
){
    val saveData = SaveData(context)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 150.dp),
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
                                    saveData.saveListToFile("Past_Expression_History",pastExpression)
                                    expanded = !expanded
                                },
                            text = "Clear",)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun GreetingPreview() {
    CalculatorAppTheme {
        MainScreen()
    }
}