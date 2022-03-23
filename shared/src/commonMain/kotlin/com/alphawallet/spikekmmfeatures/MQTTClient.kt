package com.alphawallet.spikekmmfeatures

expect class MQTTClient() {

    fun sendMessage(message:String)

    fun subscribe(topic:String, handler: (msg:String) -> Unit)
}