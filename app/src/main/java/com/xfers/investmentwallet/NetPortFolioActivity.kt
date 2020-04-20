package com.xfers.investmentwallet

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_portfolio.*
import kotlin.math.abs

class NetPortFolioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_portfolio)

        title = "Net Portfolio"

        pieChart.apply {
            updateChartSettings()
            updateChartLegends()

            // finally update data
            updateData(findData(), "User Net Portfolio")
        }
    }

    private fun findData() : Map<String, Float> {
        val merchantList = Gson().fromJson<MerchantPortfolio>(
            getJsonFromAssets(this, "data_file.json"), MerchantPortfolio::class.java
        )
        var xfers = 0
        var coin = 0
        var wowoo = 0
        for(item in merchantList) {
            if (item.merchant == "accounts@coinhako.com") {
                coin += item.value.toInt()
            }
            if (item.merchant == "binance@xfers.io") {
                xfers += item.value.toInt()
            }
            if (item.merchant == "omnibus@wowooexchange.sg") {
                wowoo += item.value.toInt()
            }
        }
        val dataMap : HashMap<String, Float> = hashMapOf()
        dataMap["accounts@coinhako.com"] = abs(coin.toFloat())
        dataMap["binance@xfers.io"] = abs(xfers.toFloat())
        dataMap["omnibus@wowooexchange.sg"] = abs(wowoo.toFloat())
        return dataMap
    }
}