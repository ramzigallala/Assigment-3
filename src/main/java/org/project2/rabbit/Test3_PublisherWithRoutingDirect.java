package org.project2.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test3_PublisherWithRoutingDirect {

  private static final String EXCHANGE_NAME = "direct_logs";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.confirmSelect();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
Thread.sleep(10000);
    String routingKey = "tag-1"; // getSeverity(argv);
    String message = "hello2"; // getMessage(argv);


    channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
    channel.addConfirmListener((sequenceNumber, multiple) -> {
      // code when message is confirmed
      System.out.println("ciao");
    }, (sequenceNumber, multiple) -> {
      // code when message is nack-ed
      System.out.println("ciao");
    });
    System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
    Thread.sleep(10000);
    channel.close();
    connection.close();
  }

  private static String getSeverity(String[] strings){
    if (strings.length < 1)
    	    return "tag-1";
    return strings[0];
  }

  private static String getMessage(String[] strings){
    if (strings.length < 2)
    	    return "Hello World!";
    return joinStrings(strings, " ", 1);
  }

  private static String joinStrings(String[] strings, String delimiter, int startIndex) {
    int length = strings.length;
    if (length == 0 ) return "";
    if (length < startIndex ) return "";
    StringBuilder words = new StringBuilder(strings[startIndex]);
    for (int i = startIndex + 1; i < length; i++) {
        words.append(delimiter).append(strings[i]);
    }
    return words.toString();
  }
}

