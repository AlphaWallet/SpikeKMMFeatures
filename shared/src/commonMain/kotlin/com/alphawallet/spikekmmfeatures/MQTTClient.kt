import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.mqtt.MqttClient


object MQTTClient {

    private var client:MqttClient;

    init {
        client = MqttClient.create(Vertx.vertx());
    }

    fun sendMessage(message:String){

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

    fun connect(callback: () -> Unit){
        client.connect(1884,"localhost").onComplete {
            println("Connection established");
            callback.invoke();
        }
    }

}