package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
    dataPoints:List<DataPoint>,
    chartStyle: ChartStyle,
    visiblePointIndices : IntRange,
    unit:String,
    modifier: Modifier = Modifier,
    selectedDataPoint: DataPoint? = null,
    onSelectedDataPoint: (DataPoint) -> Unit = {},
    onXLabelWidthChange : (Float) -> Unit = {},
    showGridLines :Boolean = true
){

    val textStyle = LocalTextStyle.current.copy(
        fontSize = chartStyle.labelFontSize
    )

    val visibleDataPoints = remember (dataPoints , visiblePointIndices){
        dataPoints.slice(visiblePointIndices)
    }

    val minYValue = remember (visibleDataPoints){
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }

    val maxYValue = remember (visibleDataPoints){
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }

    val remember = rememberTextMeasurer()

    var xLabelWidth by remember{
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }

    val selectedDataPointIndex = remember(selectedDataPoint){
        dataPoints.indexOf(selectedDataPoint)
    }

    var drawPoints by remember{
        mutableStateOf(listOf<DataPoint>())
    }

    var isShowingDataPoints by remember{
        mutableStateOf(selectedDataPoint != null)
    }

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(drawPoints , xLabelWidth){
            detectHorizontalDragGestures { change, _ ->
                val newSelectedDataPointIndex = getSelectedCoin(touchOffsetX = change.position.x,
                    triggerWidth = xLabelWidth ,
                    drawPoints = drawPoints)

                isShowingDataPoints = (newSelectedDataPointIndex + visiblePointIndices.first()) in
                        visiblePointIndices

                if(isShowingDataPoints){
                    onSelectedDataPoint(dataPoints[newSelectedDataPointIndex])
                }
            }
        }) {

        val minYLabelSpacingPx = chartStyle.minYLabelSpacing.toPx()
        val verticalPaddingPx = chartStyle.verticalPadding.toPx()
        val horizontalPaddingPx = chartStyle.horizontalPadding.toPx()
        val xLabelSpacingPx = chartStyle.xLabelSpacing.toPx()

        val xLabelTextLayoutResults = visibleDataPoints.map {
            remember.measure(
                text = it.xLabel,
                style = textStyle.copy(textAlign = TextAlign.Center)
            )
        }

        val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?:0
        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?:0
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?:0
        val xLabelLineHeight = maxXLabelHeight/maxXLabelLineCount

        val viewPortPxHeight = size.height - (2*verticalPaddingPx + maxXLabelHeight +
                xLabelSpacingPx + xLabelLineHeight)


        val labelViewPortHeight = viewPortPxHeight + xLabelLineHeight
        val labelCount = (labelViewPortHeight / (xLabelLineHeight + minYLabelSpacingPx)).toInt()

        val incrementValue = (maxYValue - minYValue)/labelCount

        val yLabels = (0..labelCount).map {
            ValueLabel(
                value = maxYValue - incrementValue*it,
                unit = unit
            )
        }

        val yLabelTextLayoutResults = yLabels.map{
            remember.measure(
                text = it.format(),
                style = textStyle
            )
        }
        val heightRequiredForLabels = xLabelLineHeight * (labelCount)
        val remainingHeightForLabels = viewPortPxHeight - heightRequiredForLabels
        val spaceBetweenLabels = remainingHeightForLabels / labelCount
        val maxYLabelWidth = yLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0

        val viewPortTopY = verticalPaddingPx + xLabelLineHeight +10f
        val viewPortBottomY = viewPortTopY + viewPortPxHeight
        val viewPortRightX = size.width
        val viewPortLeftX = 2f * horizontalPaddingPx + maxYLabelWidth

        val viewPort = Rect(top = viewPortTopY ,
                bottom = viewPortBottomY ,
                left = viewPortLeftX,
                right = viewPortRightX
            )

        yLabelTextLayoutResults.forEachIndexed { index, result ->

            val x = horizontalPaddingPx + maxYLabelWidth - result.size.width
            val y = viewPortTopY +
                    index*(xLabelLineHeight + spaceBetweenLabels) -
                    xLabelLineHeight/2f
            drawText(
                textLayoutResult = result,
                topLeft = Offset(
                    x = x,
                    y = y
                ),
                color = chartStyle.unselectedColor
            )
            if(showGridLines) {
                drawLine(
                    color = chartStyle.chartLineColor,
                    start = Offset(x = viewPortLeftX, y = y + xLabelLineHeight / 2),
                    end = Offset(
                        x = viewPortRightX,
                        y = y + xLabelLineHeight / 2
                    ),
                    strokeWidth = chartStyle.gridLineThickness
                )
            }
        }


        xLabelWidth = maxXLabelWidth + xLabelSpacingPx
        xLabelTextLayoutResults.forEachIndexed{index , result ->
            val x = viewPortLeftX + xLabelSpacingPx/2 + xLabelWidth*index
            drawText(
                textLayoutResult = result ,
                topLeft = Offset(
                    x = x,
                    y = viewPortBottomY
                ),
                color = if(index == selectedDataPointIndex) chartStyle.selectedColor
                    else chartStyle.unselectedColor
            )
            if(showGridLines) {
                drawLine(
                    color = if(index == selectedDataPointIndex) chartStyle.selectedColor
                        else chartStyle.chartLineColor,
                    start = Offset(x = x+result.size.width/2 , y = viewPortBottomY),
                    end = Offset(
                        x = x+result.size.width/2,
                        y = viewPortTopY
                    ),
                    strokeWidth = if(index == selectedDataPointIndex) chartStyle.gridLineThickness*2.25f
                    else chartStyle.gridLineThickness
                )
            }

            if(selectedDataPointIndex == index){
                val valueLabel = ValueLabel(
                    value = visibleDataPoints[index].y,
                    unit = unit
                )
                val valueResult = remember.measure(
                    text = valueLabel.format(),
                    style = textStyle.copy(color = chartStyle.selectedColor),
                    maxLines = 1
                )
                val textPositionX = if(selectedDataPointIndex == visiblePointIndices.last) {
                    x - valueResult.size.width }
                else { x - valueResult.size.width / 2 } + result.size.width/2

                val isTextInVisibleRange = (size.width - textPositionX).roundToInt() in
                        0..size.width.roundToInt()

                if(isTextInVisibleRange){
                    drawText(
                        textLayoutResult = valueResult,
                        topLeft = Offset(
                            x = textPositionX,
                            y = viewPortTopY - valueResult.size.height
                        )
                    )
                }
            }
        }

        drawPoints = visiblePointIndices.map{
            val x = viewPortLeftX -
                    xLabelSpacingPx/3 +
                    xLabelWidth*(it - visiblePointIndices.first)+
                    xLabelWidth/2f
            val ratio = (dataPoints[it].y - minYValue)/ (maxYValue - minYValue)
            val y = viewPortBottomY - ratio*viewPortPxHeight
            DataPoint(
                x = x,
                y = y,
                xLabel = dataPoints[it].xLabel
            )
        }

        val bezierPoints1= mutableListOf<DataPoint>()
        val bezierPoints2= mutableListOf<DataPoint>()

        for (i in 1 until drawPoints.size){

            val p0 = drawPoints[i-1]
            val p1 = drawPoints[i]

            val x = (p1.x + p0.x)/2
            val y0 = p0.y
            val y1 = p1.y

            bezierPoints1.add(DataPoint(x, y0 , ""))
            bezierPoints2.add(DataPoint(x, y1 , ""))

        }

        val linePath = Path().apply{

            if(drawPoints.isNotEmpty()){
                moveTo(drawPoints.first().x , drawPoints.first().y)
                for(i in 1 until drawPoints.size){
                    cubicTo(
                        x1 = bezierPoints1[i-1].x,
                        y1 = bezierPoints1[i-1].y,
                        x2 = bezierPoints2[i-1].x,
                        y2 = bezierPoints2[i-1].y,
                        x3 = drawPoints[i].x,
                        y3 = drawPoints[i].y
                    )
                }
            }

        }

        drawPath(path = linePath ,
            color = chartStyle.chartLineColor,
            style = Stroke(width = 4f))

        drawPoints.forEachIndexed { it, drawPoint ->

            if(isShowingDataPoints) {
                drawCircle(
                    color = chartStyle.unselectedColor,
                    radius = chartStyle.gridLineThickness * 10,
                    center = Offset(x = drawPoint.x, y = drawPoint.y)
                )

                if(selectedDataPointIndex == it){
                    drawCircle(
                        color =Color.White,
                        radius = chartStyle.gridLineThickness * 12,
                        center = Offset(x = drawPoint.x, y = drawPoint.y)
                    )
                    drawCircle(
                        color = chartStyle.selectedColor,
                        radius = chartStyle.gridLineThickness * 12,
                        center = Offset(x = drawPoint.x, y = drawPoint.y),
                        style = Stroke(width = 3f)
                    )
                }
            }
        }
    }

}

private fun getSelectedCoin(touchOffsetX: Float, triggerWidth:Float , drawPoints:List<DataPoint>):Int{

    val triggerRangeLeft = touchOffsetX - triggerWidth/2
    val triggerRangeRight = touchOffsetX + triggerWidth/2
    return drawPoints.indexOfFirst {    it.x in triggerRangeLeft..triggerRangeRight   }

}


@Preview(widthDp = 800)
@Composable
fun lineChart(){

    CryptoTrackerTheme {
        val coinHistoryRandomized = remember {
            (1..20).map {
                CoinPrice(
                    priceUsd = Random.nextDouble() * 1000,
                    time = ZonedDateTime.now().plusHours(it.toLong())
                )
            }
        }

        val chartStyle = ChartStyle(
            chartLineColor = Color.Gray,
            unselectedColor = Color.LightGray,
            selectedColor = Color.Magenta,
            gridLineThickness = 1f,
            axisLineThickness = 1f,
            labelFontSize = 12.sp,
            minYLabelSpacing = 12.dp,
            verticalPadding = 10.dp,
            horizontalPadding = 8.dp,
            xLabelSpacing = 12.dp
        )

        val dataPoints = remember {
            coinHistoryRandomized.map {
                DataPoint(
                    x = it.time.hour.toFloat(),
                    y = it.priceUsd.toFloat(),
                    xLabel = DateTimeFormatter.ofPattern("ha\nM/d")
                        .format(it.time)
                )
            }
        }

        LineChart(
            dataPoints = dataPoints,
            chartStyle = chartStyle,
            visiblePointIndices = 0..19,
            unit = "$",
            modifier = Modifier
                .width(700.dp)
                .height(300.dp)
                .background(Color.White),
            selectedDataPoint = dataPoints[2]
        )
    }

}