package com.alphawallet.spikekmmfeatures.android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alphawallet.spikekmmfeatures.Greeting
import com.alphawallet.spikekmmfeatures.KFetcher
import com.ionspin.kotlin.bignum.integer.BigInteger

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = String.format("%s\n%s", greet(), add())
    }

    private fun add(): String {
        val fetcher = KFetcher()
        val i = BigInteger.fromLong(Long.MAX_VALUE)
        val sum = fetcher.addOne(i)
        return String.format("Long.MAX_VALUE + 1 = %s", sum.toString())
    }
}
