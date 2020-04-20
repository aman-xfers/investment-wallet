package com.xfers.investmentwallet

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_monthly.*
import java.util.*

class MonthlyInvestmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_monthly)

        title = "Monthly Investment"


        monthlyChart.getDescription().setEnabled(false)

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        monthlyChart.setPinchZoom(false)

        monthlyChart.setDrawBarShadow(false)

        monthlyChart.setDrawGridBackground(false)

        val l = monthlyChart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(true)
        l.setYOffset(0f)
        l.setXOffset(10f)
        l.setYEntrySpace(0f)
        l.setTextSize(8f)

        val xAxis = monthlyChart.getXAxis()
        xAxis.setGranularity(1f)
        xAxis.setCenterAxisLabels(true)
        xAxis.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })

        val leftAxis = monthlyChart.getAxisLeft()
        leftAxis.setValueFormatter(LargeValueFormatter())
        leftAxis.setDrawGridLines(false)
        leftAxis.setSpaceTop(35f)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)

        monthlyChart.getAxisRight().setEnabled(false)
    }

    private fun findData() {
        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        val groupCount = 12
        val startYear = 1
        val endYear = groupCount

        val merchantList = Gson().fromJson<MerchantPortfolio>(
            getJsonFromAssets(this, "data_file.json"), MerchantPortfolio::class.java
        )

        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()
        val values3 = ArrayList<BarEntry>()
        val values4 = ArrayList<BarEntry>()

        for (i in startYear until endYear) {
            values1.add(BarEntry(i.toFloat(), merchantList[i].value))
        }

        val set1: BarDataSet
        val set2: BarDataSet
        val set3: BarDataSet
        val set4: BarDataSet

        if (monthlyChart.getData() != null && monthlyChart.getData().getDataSetCount() > 0) {

            set1 = monthlyChart.getData().getDataSetByIndex(0) as BarDataSet
            set2 = monthlyChart.getData().getDataSetByIndex(1) as BarDataSet
            set3 = monthlyChart.getData().getDataSetByIndex(2) as BarDataSet
            set4 = monthlyChart.getData().getDataSetByIndex(3) as BarDataSet
            set1.values = values1
            set2.values = values2
            set3.values = values3
            set4.values = values4
            monthlyChart.getData().notifyDataChanged()
            monthlyChart.notifyDataSetChanged()

        } else {
            // create 4 DataSets
            set1 = BarDataSet(values1, "Company A")
            set1.color = Color.rgb(104, 241, 175)
            set2 = BarDataSet(values2, "Company B")
            set2.color = Color.rgb(164, 228, 251)
            set3 = BarDataSet(values3, "Company C")
            set3.color = Color.rgb(242, 247, 158)
            set4 = BarDataSet(values4, "Company D")
            set4.color = Color.rgb(255, 102, 0)

            val data = BarData(set1, set2, set3, set4)
            data.setValueFormatter(LargeValueFormatter())

            monthlyChart.setData(data)
        }

        // specify the width each bar should have
        monthlyChart.getBarData().setBarWidth(barWidth)

        // restrict the x-axis range
        monthlyChart.getXAxis().setAxisMinimum(startYear.toFloat())

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        monthlyChart.getXAxis().setAxisMaximum(
            startYear + monthlyChart.getBarData().getGroupWidth(
                groupSpace,
                barSpace
            ) * groupCount
        )
        monthlyChart.groupBars(startYear.toFloat(), groupSpace, barSpace)
        monthlyChart.invalidate()
    }
}