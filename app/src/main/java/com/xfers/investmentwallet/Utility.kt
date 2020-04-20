package com.xfers.investmentwallet

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

fun getJsonFromAssets(context: Context, fileName: String) : String {
    try {
        val iStream = context.assets.open(fileName)
        val size = iStream.available()
        val buffer = ByteArray(size)
        iStream.read(buffer)
        iStream.close()

        return String(buffer, Charset.defaultCharset())
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    return ""
}