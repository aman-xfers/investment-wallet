package com.xfers.investmentwallet

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_merchant.*
import java.text.DecimalFormat
import java.util.ArrayList

class MerchantPortfolioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_merchant)

        title = "Xfers Portfolio"

        merchantPortfolioChart.setBackgroundColor(Color.WHITE)
        merchantPortfolioChart.setExtraTopOffset(-30f)
        merchantPortfolioChart.setExtraBottomOffset(10f)
        merchantPortfolioChart.setExtraLeftOffset(70f)
        merchantPortfolioChart.setExtraRightOffset(70f)

        merchantPortfolioChart.setDrawBarShadow(false)
        merchantPortfolioChart.setDrawValueAboveBar(true)

        merchantPortfolioChart.getDescription().setEnabled(false)

        // scaling can now only be done on x- and y-axis separately
        merchantPortfolioChart.setPinchZoom(false)

        merchantPortfolioChart.setDrawGridBackground(false)

        val xAxis = merchantPortfolioChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setTextColor(Color.LTGRAY)
        xAxis.setTextSize(13f)
        xAxis.setLabelCount(5)
        xAxis.setCenterAxisLabels(true)
        xAxis.setGranularity(1f)

        val left = merchantPortfolioChart.getAxisLeft()
        left.setDrawLabels(false)
        left.setSpaceTop(25f)
        left.setSpaceBottom(25f)
        left.setDrawAxisLine(false)
        left.setDrawGridLines(false)
        left.setDrawZeroLine(true) // draw a zero line
        left.setZeroLineColor(Color.GRAY)
        left.setZeroLineWidth(0.7f)
        merchantPortfolioChart.getAxisRight().setEnabled(false)
        merchantPortfolioChart.getLegend().setEnabled(false)

        merchantPortfolioChart.animateY(1500, Easing.EaseInOutQuad)
        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT

        val merchantList = Gson().fromJson<MerchantPortfolio>(
            getJsonFromAssets(this, "data_file.json"), MerchantPortfolio::class.java
        )
        val merchant = "binance@xfers.io"
        val dataList = merchantList.filter {
            it.merchant == merchant
        }

        val data = ArrayList<Data>()
        for (i in 0 until dataList.size - 1) {
            data.add(Data(i.toFloat(), dataList[i].value, dataList[i].created_at.substring(0,7)))
        }

        xAxis.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return data[Math.min(Math.max(value.toInt(), 0), data.size - 1)].xAxisValue
            }
        })

        setData(data)
    }

    private fun setData(dataList: List<Data>) {

        val values = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()

        val green = Color.rgb(110, 190, 102)
        val red = Color.rgb(211, 74, 88)

        for (i in dataList.indices) {

            val d = dataList[i]
            val entry = BarEntry(d.xValue, d.yValue)
            values.add(entry)

            // specific colors
            if (d.yValue >= 0)
                colors.add(red)
            else
                colors.add(green)
        }

        val set: BarDataSet

        if (merchantPortfolioChart.getData() != null && merchantPortfolioChart.getData().getDataSetCount() > 0) {
            set = merchantPortfolioChart.getData().getDataSetByIndex(0) as BarDataSet
            set.values = values
            merchantPortfolioChart.getData().notifyDataChanged()
            merchantPortfolioChart.notifyDataSetChanged()
        } else {
            set = BarDataSet(values, "Values")
            set.colors = colors
            set.setValueTextColors(colors)

            val data = BarData(set)
            data.setValueTextSize(13f)
            data.setValueFormatter(Formatter())
            data.barWidth = 0.8f

            merchantPortfolioChart.setData(data)
            merchantPortfolioChart.invalidate()
        }
    }

    /**
     * Demo class representing data.
     */
    private inner class Data internal constructor(
        internal val xValue: Float,
        internal val yValue: Float,
        internal val xAxisValue: String
    )

    private inner class Formatter internal constructor() : ValueFormatter() {

        private val mFormat: DecimalFormat

        init {
            mFormat = DecimalFormat("######.0")
        }

        override fun getFormattedValue(value: Float): String {
            return mFormat.format(value.toDouble())
        }
    }
}