package com.alphawallet.spikekmmfeatures

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KFetcher {
    fun addOne(i: BigInteger): BigInteger {
        return i + 1
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetch(block: (KURLAndString) -> Int) {
        val client = HttpClient()
        GlobalScope.launch() {
            val urlString = "https://api.myip.com"
            val body: String = client.get {
                url(urlString)
            }

            val result = KURLAndString(urlString, body)
            val i = block(result)
            print(i)
        }
    }

    fun decrement(i: BigInteger, dec: KFoo): BigInteger {
        return dec.decrement(i)
    }
}

interface KFoo {
    fun decrement(i: BigInteger): BigInteger
}

data class KURLAndString (
    val url: String, // no URL object
    val string: String
)
