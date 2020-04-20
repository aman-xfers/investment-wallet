package com.xfers.investmentwallet

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.github.mikephil.charting.components.Legend
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        title = "Investment Wallet"

        portfolio.setOnClickListener {
            startActivity(Intent(this, NetPortFolioActivity::class.java))
        }

        merchantPortfolio.setOnClickListener {
            startActivity(Intent(this, MerchantPortfolioActivity::class.java))
        }

        monthly.setOnClickListener {
            startActivity(Intent(this, MonthlyInvestmentActivity::class.java))
        }

        val merchantList = Gson().fromJson<MerchantPortfolio>(
            getJsonFromAssets(this, "data_file.json"), MerchantPortfolio::class.java
        )

        var totalAmount = 0
        for (item in merchantList) {
            totalAmount += item.value.toInt()
        }

        assets_text.text = assets_text.text.toString() + "$ " + totalAmount
    }
}
