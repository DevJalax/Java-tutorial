import org.eclipse.paho.client.mqttv3.*;

public class MQTTClient {
    private final String brokerUrl;
    private final String clientId;
    private MqttClient mqttClient;

    public MQTTClient(String brokerUrl, String clientId) {
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
        try {
            mqttClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            mqttClient.connect(options);
            System.out.println("Connected to MQTT broker: " + brokerUrl);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2); // Quality of Service level
            mqttClient.publish(topic, mqttMessage);
            System.out.println("Published message: " + message + " to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            mqttClient.subscribe(topic, (t, msg) -> {
                String message = new String(msg.getPayload());
                System.out.println("Received message: " + message + " from topic: " + t);
                // Process the received message (e.g., update streetlight status)
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mqttClient.disconnect();
            System.out.println("Disconnected from MQTT broker");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
