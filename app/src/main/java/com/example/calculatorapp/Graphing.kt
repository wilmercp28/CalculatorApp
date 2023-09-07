package com.example.calculatorapp

import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

@Composable
fun MainScreenGraphing(

) {
    val steps = remember{ mutableStateOf("") }
    val yValues: MutableList<Double> = rememberSaveable{ mutableListOf() }
    val xValues: MutableList<Double> = remember { mutableListOf() }
    val buttonsSeparation = 5.dp
    val buttonsSize = 200.dp
    val yVerticalStep = rememberSaveable{ mutableStateOf(1) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ){
        CanvasGraph(xValues, yValues,5.dp,yVerticalStep)
        TextFieldGraph(steps,yValues,xValues,yVerticalStep)
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
                            steps
                            )
                        Spacer(modifier = Modifier.size(buttonsSeparation))
                    }
                }
            }
        }
    }
}

@Composable
fun CanvasGraph(
    xValues: List<Double>,
    yValues: List<Double>,
    paddingSpace: Dp,
    verticalStep: MutableState<Int>
){
    val circleColor = MaterialTheme.colorScheme.onPrimary
    val controlPoints1 = rememberSaveable{mutableListOf<PointF>()}
    val controlPoints2 = rememberSaveable{mutableListOf<PointF>()}
    val coordinates = rememberSaveable{mutableListOf<PointF>()}
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = Modifier
            .height(350.dp)
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            coordinates.clear()
            controlPoints1.clear()
            controlPoints2.clear()
            if (yValues.isNotEmpty() && xValues.isNotEmpty()) {
                val maxX = xValues.maxOrNull() ?: 1.0
                val maxY = yValues.maxOrNull() ?: 1.0
                val xScale = (size.width - paddingSpace.toPx()) / maxX.toFloat()
                val yScale = size.height / maxY.toFloat()
                val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
                val yAxisSpace = size.height / yValues.size
                for (i in xValues.indices) {
                    val x = xValues[i].toFloat() * xScale
                    val y = size.height - yValues[i].toFloat() * yScale
                    coordinates.add(PointF(x, y))
                    drawContext.canvas.nativeCanvas.drawText(
                        "${xValues[i]}",
                        x,
                        size.height - 30,
                        textPaint
                    )
                }
                /** placing x axis points */
                for (i in xValues.indices) {
                    drawContext.canvas.nativeCanvas.drawText(
                        "${xValues[i]}",
                        xAxisSpace * (i + 1),
                        size.height - 30,
                        textPaint
                    )
                }
                /** placing y axis points */
                for (i in yValues.indices) {
                    if (i % verticalStep.value == 0) {
                        drawContext.canvas.nativeCanvas.drawText(
                            "${yValues[i]}",
                            paddingSpace.toPx() / 2f,
                            size.height - yAxisSpace * (i + 1),
                            textPaint
                        )
                    }
                }

                /** calculating the connection points */
                for (i in 1 until coordinates.size) {
                    controlPoints1.add(
                        PointF(
                            (coordinates[i].x + coordinates[i - 1].x) / 2,
                            coordinates[i - 1].y
                        )
                    )
                    controlPoints2.add(
                        PointF(
                            (coordinates[i].x + coordinates[i - 1].x) / 2,
                            coordinates[i].y
                        )
                    )
                }
                /** drawing the path */
                val stroke = Path().apply {
                    reset()
                    moveTo(coordinates.first().x, coordinates.first().y)
                    for (i in 0 until coordinates.size - 1) {
                        cubicTo(
                            controlPoints1[i].x, controlPoints1[i].y,
                            controlPoints2[i].x, controlPoints2[i].y,
                            coordinates[i + 1].x, coordinates[i + 1].y
                        )
                        drawCircle(circleColor, radius = 4.dp.toPx(), center = Offset(coordinates[i].x, coordinates[i].y))
                    }
                }

                /** filling the area under the path */
                val fillPath = android.graphics.Path(stroke.asAndroidPath())
                    .asComposePath()
                    .apply {
                        lineTo(xAxisSpace * xValues.last().toFloat(), size.height - yAxisSpace)
                        lineTo(xAxisSpace, size.height - yAxisSpace)
                        close()
                    }
                drawPath(
                    fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Cyan,
                            Color.Transparent,
                        ),
                        endY = size.height - yAxisSpace
                    ),
                )
                drawPath(
                    stroke,
                    color = Color.Black,
                    style = Stroke(
                        width = 5f,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextFieldGraph(
    text: MutableState<String>,
    yValuesList: MutableList<Double>,
    xValuesList: MutableList<Double>,
    yVerticalStep: MutableState<Int>,
){
    val xValues = remember { mutableStateOf(1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            content = {
                item { Text(text = yValuesList.toString().removePrefix("[").removeSuffix("]")) }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Row {
            Column(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(5.dp)
                    )
                    .combinedClickable(
                        onClick = {
                            yValuesList += text.value.toDouble()
                            text.value = ""
                            xValuesList += xValues.value.toDouble()
                            xValues.value++
                        },
                        onLongClick = {
                            yValuesList.clear()
                            xValuesList.clear()
                            xValues.value = 1
                            text.value = ""
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = text.value,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    maxLines = 1
                )
                Divider()
                Text(
                    text = "+ Step",
                    textAlign = TextAlign.Center
                )
            }
            Column {
                Row {
                    Button(
                        onClick = {
                            if (yVerticalStep.value > 1){
                                yVerticalStep.value--
                            }
                        }) {
                        Text(text = "-")
                    }
                    Button(
                        onClick = {
                            yVerticalStep.value++
                        }) {
                        Text(text = "+")
                    }
                }
                Text(text = "Y Vertical Step ${yVerticalStep.value}" )
            }
        }
    }

}

@Composable
fun KeyPadButtonsOnlyNumbers(
    symbol: String,
    backgroundColor: Color,
    buttonsSize: Dp,
    steps: MutableState<String>
){
    Box(
        modifier = Modifier
            .size(buttonsSize / 3)
            .background(backgroundColor, RoundedCornerShape(buttonsSize))
            .clickable {
                if (symbol == "<" && steps.value.isNotEmpty()) {
                    steps.value = steps.value.substringBeforeLast(steps.value.last())
                } else if (symbol != "<" && steps.value.length < 9) {
                    steps.value += symbol
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

