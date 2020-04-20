package com.xfers.investmentwallet

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

/**
 * method to update how pie chart will look like
 */
fun PieChart.updateChartSettings() {

    setExtraOffsets(5f, 5f, 5f, 5f)

    setDrawRoundedSlices(false)

    setUsePercentValues(true)

    isDrawHoleEnabled = true
    holeRadius = 58f
    setHoleColor(Color.WHITE)

    dragDecelerationFrictionCoef = 0.95f

    setTransparentCircleColor(Color.WHITE)
    setTransparentCircleAlpha(110)
    transparentCircleRadius = 60f

    rotationAngle = 0f

    isHighlightPerTapEnabled = true

    // entry label styling
    setDrawEntryLabels(false)
    setEntryLabelColor(Color.BLACK)
    setEntryLabelTextSize(15f)

    animateY(1500, Easing.EaseInOutQuad)

    setDrawCenterText(true)
    description.isEnabled = false
}

/**
 * method to set legends positions
 */
fun PieChart.updateChartLegends() {
    legend.apply {
        verticalAlignment = Legend.LegendVerticalAlignment.TOP
        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        orientation = Legend.LegendOrientation.VERTICAL
        setDrawInside(false)
        xEntrySpace = 8f
        yEntrySpace = 6f
        yOffset = 0f
        isWordWrapEnabled = true
        textSize = 14f
    }
}

/**
 * update data on to the given chart
 */
fun PieChart.updateData(dataMap: Map<String, Float>, label: String) {
    val entries = ArrayList<PieEntry>()

    for (entry in dataMap.entries) {
        entries.add(
            PieEntry(entry.value, entry.key)
        )
    }
    val dataSet = PieDataSet(entries, label)
    dataSet.setDrawIcons(false)
    dataSet.sliceSpace = 3f
    dataSet.iconsOffset = MPPointF(0f, 40f)
    dataSet.selectionShift = 5f
    // add a lot of colors
    val colors = ArrayList<Int>()
    for (c in ColorTemplate.MATERIAL_COLORS)
        colors.add(c)
    colors.add(ColorTemplate.getHoloBlue())

    dataSet.colors = colors

    val data = PieData(dataSet)
    data.setValueFormatter(PercentFormatter(this))
    data.setValueTextSize(16f)
    data.setValueTextColor(Color.WHITE)

    setData(data)
    // undo all highlights
    highlightValues(null)
    invalidate()
    centerText = getCenterSpannableText()
}

fun PieChart.getCenterSpannableText(): SpannableString {
    val totalValue = data.yValueSum.toString()
    val s = SpannableString("$totalValue\nTotal")
    s.setSpan(RelativeSizeSpan(1.8f), 0, totalValue.length, 0)
    s.setSpan(StyleSpan(Typeface.BOLD), 0, totalValue.length, 0)
    s.setSpan(ForegroundColorSpan(Color.GRAY), s.length - 5, s.length, 0)
    s.setSpan(RelativeSizeSpan(1f), s.length - 5, s.length, 0)
    return s
}