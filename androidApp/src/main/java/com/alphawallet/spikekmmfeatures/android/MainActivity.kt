package com.alphawallet.spikekmmfeatures.android

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alphawallet.spikekmmfeatures.Greeting
import com.alphawallet.spikekmmfeatures.KFetcher
import com.alphawallet.spikekmmfeatures.KURLAndString
import com.ionspin.kotlin.bignum.integer.BigInteger

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.text_view)
        tv.text = String.format("%s\n%s", greet(), add())

        fetch()
    }

    private fun fetch() {
        KFetcher().fetch {
            Log.d("kmm", "Received (urlAndString)")
            Log.d("kmm", "Received (${it.url})")
            Log.d("kmm", "Received (${it.string})")
            runOnUiThread{
                tv.text = String.format("%s\n%s", tv.text, it.string)
            }
            return@fetch 456
        }
    }

    private fun add(): String {
        val fetcher = KFetcher()
        val i = BigInteger.fromLong(Long.MAX_VALUE)
        val sum = fetcher.addOne(i)
        return String.format("Long.MAX_VALUE + 1 = %s", sum.toString())
    }
}
