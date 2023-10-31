package org.project2.rabbit.messages_manager;


import com.rabbitmq.client.*;
import org.project2.rabbit.message.StatusManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class ClientSender {
    private static final String EXCHANGE_NAME = "direct_messages";
    private final String routingKeyStart;
    private final String routingKeyUpdate;
    private final String routingKeyExit;
    private final Channel channel;
    private final String consumerTag;

    public ClientSender(Channel channel, String consumerTag) throws IOException, InterruptedException, TimeoutException {
        this.channel=channel;
        // getSeverity(argv);
        routingKeyStart = "start";
        // getSeverity(argv);
        routingKeyUpdate = "update";
        routingKeyExit = "exit";

        this.consumerTag=consumerTag;


    }

    public void requestStart(StatusManager statusManager){
        //sendStatus(routingKeyStart, "start".getBytes());

        //sendStatus("update", Optional.of());
        //tre canali: 1 richiede start, 1 ricevere lo start, 1 per i messaggi di update
        sendStatus(routingKeyStart,statusManager.getStatus());
    }

    public void requestStart(){
        StatusManager emptyStatus = new StatusManager();
        sendStatus(routingKeyStart, emptyStatus.getStatus());
    }

    public void sendUpdate(byte[] message){
        sendStatus(routingKeyUpdate, message);
    }

    public void sendExit(String consumerTag){
        try {
            sendStatus(routingKeyExit, consumerTag.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendStatus(String routingKey, byte[] message){
        try {
            //System.out.println("dentro modifica grid");
            AMQP.BasicProperties messageProperties = new AMQP.BasicProperties.Builder()
                    .messageId(consumerTag)
                    .deliveryMode(1)
                    .build();


            //channel.basicPublish(EXCHANGE_NAME, routingKey, messageProperties, message);
            channel.basicPublish(EXCHANGE_NAME, routingKey, messageProperties, message);
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'"+consumerTag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
