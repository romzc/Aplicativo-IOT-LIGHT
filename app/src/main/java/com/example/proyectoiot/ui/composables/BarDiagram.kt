package com.example.proyectoiot.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectoiot.ui.theme.Green
import com.example.proyectoiot.ui.theme.LightPink
import com.example.proyectoiot.ui.theme.Orange
import com.example.proyectoiot.ui.theme.Red
import com.example.proyectoiot.ui.theme.Yellow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun BarChart(
    inputList: List<ObjectData>,
    modifier: Modifier = Modifier,
    showDescription: Boolean
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        val listSum by remember { mutableStateOf(inputList.sumOf { it.DeviceData.luminosity }) }
        inputList.forEachIndexed { index, input -> // Agrega el índice usando forEachIndexed
            val percentage = input.DeviceData.luminosity / listSum.toFloat()
            val isLastIndex = index == inputList.lastIndex
            val primaryColor = getColorByPercentage(percentage)
            Bar(
                modifier = Modifier
                    .height(120.dp * percentage * inputList.size)
                    .width(40.dp),
                primaryColor = primaryColor,
                secondaryColor = if (isLastIndex) LightPink else Color.White,
                luminosity = input.DeviceData.luminosity,
                description = getDayOfWeekFromDate(input.DeviceData.calendar),
                showDescription = showDescription
            )
        }
    }
}

@Composable
fun Bar(
    modifier: Modifier = Modifier,
    primaryColor: Color,
    secondaryColor: Color,
    luminosity: Int,
    description: String,
    showDescription: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier = Modifier.fillMaxSize()
        ){
            val width = size.width
            val height = size.height
            val barWidth = width / 5 * 3
            val barHeight = height / 8 * 7
            val barHeight3DPart = height - barHeight
            val barWidth3DPart = (width - barWidth)*(height*0.002f)

            var path = Path().apply {
                moveTo(0f,height)
                lineTo(barWidth,height)
                lineTo(barWidth,height-barHeight)
                lineTo(0f,height-barHeight)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, Color.Black)
                )
            )
            path = Path().apply {
                moveTo(barWidth,height-barHeight)
                lineTo(barWidth3DPart+barWidth,0f)
                lineTo(barWidth3DPart+barWidth,barHeight)
                lineTo(barWidth,height)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor,primaryColor, Color.Black  )
                )
            )
            path = Path().apply {
                moveTo(0f,barHeight3DPart)
                lineTo(barWidth,barHeight3DPart)
                lineTo(barWidth+barWidth3DPart,0f)
                lineTo(barWidth3DPart,0f)
                close()
            }
            drawPath(
                path,
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, Color.Black)
                )
            )
            val heightAdd = 80f
            val addCorner = 5f
            val cornerRadius = 5.dp.toPx()
            val textPaint = android.graphics.Paint().apply {
                color = secondaryColor.toArgb()
                textSize = 17.dp.toPx()
                isFakeBoldText = true
            }

            drawContext.canvas.nativeCanvas.apply {
                drawRoundRect(
                    0f - addCorner,
                    height - 70f + heightAdd - addCorner,
                    barWidth + addCorner,
                    height + heightAdd + addCorner,
                    cornerRadius,
                    cornerRadius,
                    android.graphics.Paint().apply {
                        color = Color.Black.toArgb() // Puedes ajustar el color del fondo aquí
                    }
                )

                val textWidth = textPaint.measureText(description)
                val textX = (barWidth - textWidth - 5f) / 2f
                val textY = heightAdd - 14f + height
                drawText(description, textX, textY, textPaint)
            }

            if (showDescription) {
                val percentageText = " $luminosity"

                val textPaint = android.graphics.Paint().apply {
                    color = Color.White.toArgb()
                    textSize = 10.dp.toPx()
                    isFakeBoldText = true
                }

                val signPaint = android.graphics.Paint().apply {
                    color = Color.White.toArgb()
                    textSize = 10.dp.toPx()
                    isFakeBoldText = true
                }

                drawContext.canvas.nativeCanvas.apply {
                    val textWidth = textPaint.measureText(percentageText)
                    val textX = (barWidth - textWidth- 20f) / 2f
                    val metrics = textPaint.fontMetrics
                    val textHeight = metrics.bottom - metrics.top
                    val textY = height / 2f + textHeight / 2f
                    drawText(percentageText, textX, textY, textPaint)
                    val signX = textX + textWidth
                    val signMetrics = signPaint.fontMetrics
                    val signY = textY
                    drawText("l", signX, signY, signPaint)
                }
            }
        }
    }
}

private fun getDayOfWeekFromDate(dateString: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = sdf.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    return when (dayOfWeek) {
        Calendar.SUNDAY -> "D"
        Calendar.MONDAY -> "L"
        Calendar.TUESDAY -> "M"
        Calendar.WEDNESDAY -> "M"
        Calendar.THURSDAY -> "J"
        Calendar.FRIDAY -> "V"
        Calendar.SATURDAY -> "S"
        else -> "N/A"
    }
}



@Composable
fun getColorByPercentage(percentage: Float): Color {
    val colors = listOf(
        Green,
        Yellow,
        Orange,
        Red
    )

    val thresholds = listOf(0.0f, 0.15f, 35f, 0.55f, 1.0f)

    for (i in 0 until thresholds.size - 1) {
        if (percentage >= thresholds[i] && percentage < thresholds[i + 1]) {
            return colors[i]
        }
    }

    return colors.last()
}