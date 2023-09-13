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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
import kotlinx.coroutines.launch

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
                                "0" ->  AlgebraicCalculator()
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


@Preview
@Composable
fun GreetingPreview() {
    CalculatorAppTheme {
        AlgebraicCalculator()
    }
}