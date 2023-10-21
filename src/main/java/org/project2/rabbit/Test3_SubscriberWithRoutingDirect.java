package org.project2.rabbit;

import com.rabbitmq.client.*;

public class Test3_SubscriberWithRoutingDirect {

  private static final String EXCHANGE_NAME = "direct_logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.confirmSelect();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    String queueName = channel.queueDeclare().getQueue();


    channel.queueBind(queueName, EXCHANGE_NAME, "tag-1");
    channel.queueBind(queueName, EXCHANGE_NAME, "tag-2");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});


  }
}

