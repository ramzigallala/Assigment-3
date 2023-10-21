package org.project2.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class ClientSender {
    private static final String EXCHANGE_NAME = "direct_messages";
    private final String routingKeyStart;
    private final String routingKeyUpdate;
    private final Channel channel;

    public ClientSender() throws IOException, InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.confirmSelect();
        channel.waitForConfirmsOrDie(1000);

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // getSeverity(argv);
        routingKeyStart = "start";
        // getSeverity(argv);
        routingKeyUpdate = "update";


    }

    public void sendStart(){
        //sendStatus(routingKeyStart, "start".getBytes());
    }
    public void sendUpdate(byte[] message){
        sendStatus(routingKeyUpdate, message);
    }

    private void sendStatus(String routingKey, byte[] message){
        try {
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message);
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
