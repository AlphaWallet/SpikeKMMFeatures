package com.alphawallet.spikekmmfeatures.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alphawallet.spikekmmfeatures.Greeting
import com.alphawallet.spikekmmfeatures.KFetcher
import com.alphawallet.spikekmmfeatures.KFoo
import com.alphawallet.spikekmmfeatures.MQTTClient
import com.ionspin.kotlin.bignum.integer.BigInteger
import org.json.JSONObject
import java.io.InputStream
import java.net.URL


fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    lateinit var tv: TextView
    lateinit var dataText: TextView
    lateinit var dataImage: ImageView
    lateinit var mqttClient: MQTTClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.text_view)
        tv.text = String.format("%s\n%s", greet(), add())

        dataText = findViewById(R.id.data_text)
        dataImage = findViewById(R.id.data_image)

        fetch()
        decrement()

        connectMessageQueue()
    }

    private fun connectMessageQueue(){

        mqttClient = MQTTClient()

        mqttClient.subscribe("test") { msg: String ->

            // TODO: Handle event and save data to local storage in kotlin common code

            //println("Message received")
            //println(msg)

            var data:JSONObject;

            try {
                data = JSONObject(msg);
            } catch (e:Throwable){
                println(e.message)
                return@subscribe
            }

            data = data.getJSONObject("data")

            try {
                val metadata = data.getJSONObject("metadata")

                runOnUiThread {
                    dataText.setText(metadata.toString())

                    DownloadImageTask(dataImage).execute(metadata.getString("image"));
                }

            } catch (e:Throwable){
                println("Error showing metadata: " + e.message)
                return@subscribe
            }

        }
    }

    private fun decrement() {
        val result = KFetcher().decrement(BigInteger.ONE, AFoo())
        appendText(String.format("1 - 1 = %s", result.toString()))
    }

    private fun fetch() {
        KFetcher().fetch {
            Log.d("kmm", "Received (urlAndString)")
            Log.d("kmm", "Received (${it.url})")
            Log.d("kmm", "Received (${it.string})")
            appendText(it.string)
            return@fetch 456
        }
    }

    private fun appendText(args: String) {
        runOnUiThread {
            tv.text = String.format("%s\n%s", tv.text, args)
        }
    }

    private fun add(): String {
        val fetcher = KFetcher()
        val i = BigInteger.fromLong(Long.MAX_VALUE)
        val sum = fetcher.addOne(i)
        return String.format("Long.MAX_VALUE + 1 = %s", sum.toString())
    }
}

class AFoo : KFoo() {
    override fun decrement(i: BigInteger): BigInteger {
        return i - 1
    }
}

internal class DownloadImageTask(var bmImage: ImageView) :
    AsyncTask<String?, Void?, Bitmap?>() {

    protected override fun doInBackground(vararg urls: String?): Bitmap? {
        val urldisplay = urls[0]
        var mIcon11: Bitmap? = null
        try {
            val input: InputStream = URL(urldisplay).openStream()
            mIcon11 = BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
        }
        return mIcon11
    }

    protected override fun onPostExecute(result: Bitmap?) {
        bmImage.setImageBitmap(result)
    }
}
