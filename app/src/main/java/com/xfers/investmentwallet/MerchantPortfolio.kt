package com.xfers.investmentwallet

class MerchantPortfolio : ArrayList<MerchantPortfolio.MerchantPortfolioItem>(){
    data class MerchantPortfolioItem(
        val created_at: String,
        val merchant: String,
        val value: Float
    )
}