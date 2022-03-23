package com.alphawallet.spikekmmfeatures

import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.mqtt.MqttClient



actual class MQTTClient actual constructor() {

    private var client:MqttClient;

    init {
        client = MqttClient.create(Vertx.vertx());
    }

    actual fun sendMessage(message:String){

        if (!client.isConnected){
            connect({ sendMessage(message) })
            return;
        }

        client.publish(
            "test",
            Buffer.buffer(message),
            MqttQoS.AT_MOST_ONCE, // QoS > 0 does not seem to be working at the moment. does KMQTT send proper acknowledgements? does it need to be configured?
            false,
            false
        ).onSuccess {
            println("Message published")
        }.onFailure { e -> println("Publish failed: " + e.message) }
    }

    actual fun subscribe(topic: String, handler: (msg:String) -> Unit) {

        if (!client.isConnected){
            connect({ subscribe(topic, handler) })
            return
        }

        client.publishHandler { s ->
            System.out.println("There are new message in topic: " + s.topicName());
            System.out.println("Content(as string) of the message: " + s.payload().toString());
            System.out.println("QoS: " + s.qosLevel());
            handler(s.payload().toString())
        }.subscribe(topic, MqttQoS.AT_MOST_ONCE.value()).onSuccess {
            println("Subscribe success")
        }.onFailure {
            println("Subscribe failed: " + it.message)
        }
    }

    fun connect(callback: () -> Unit){
        client.connect(1884,"192.168.1.15").onSuccess {
            println("Connection established");
            callback.invoke();
        }.onFailure {
            println("Failed to connect to MQTT broker: " + it.message)
        }
    }

}