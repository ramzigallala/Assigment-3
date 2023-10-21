package org.project2.rabbit;

import com.rabbitmq.client.*;
import org.apache.commons.lang3.SerializationUtils;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ClientListener {
    private static final String EXCHANGE_NAME = "direct_messages";
    private final BrushManager brushManager;
    private final PixelGridView view;
    private final String consumerTag;
    private final PixelGrid grid;
    public ClientListener(BrushManager brushManager, PixelGridView view, PixelGrid grid) throws IOException, TimeoutException {
        this.brushManager = brushManager;
        this.view=view;
        this.grid=grid;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();


        channel.queueBind(queueName, EXCHANGE_NAME, "start");
        channel.queueBind(queueName, EXCHANGE_NAME, "update");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");



        consumerTag= channel.basicConsume(queueName, false, setCallback(), consumerTag -> {});
    }
    private DeliverCallback setCallback(){
        return new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery deliveredMessage) throws IOException {

                //String message = new String(deliveredMessage.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + deliveredMessage.getEnvelope().getRoutingKey() + "':'" + "arrivato" + "'");
                if(deliveredMessage.getEnvelope().getRoutingKey().equals("update")){
                    updateView(deliveredMessage.getBody());

                }else {

                }
                view.refresh();

            }
        };
    }

    private void updateView(byte[] message){
        //System.out.println("mio "+brushManager.getBrushes().get(0).getX());
        StatusManager statusManager = SerializationUtils.deserialize(message);

        BrushManager brushManagerDelivered = statusManager.getBrushManager();
        PixelGrid gridDelivered = statusManager.getGrid();
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int column = 0; column < grid.getNumColumns(); column++) {
                grid.getGrid()[row][column]=gridDelivered.getGrid()[row][column];

            }
        }

        //System.out.println("arrivato "+brushManagerDelivered.getBrushes().get(0).getX());
        //brushManager.getBrushes().clear();
        //brushManager.getBrushes().addAll(brushManagerDelivered.getBrushes());

        brushManagerDelivered.getBrushes().forEach(brush -> {
            List<BrushManager.Brush> brushLive = brushManager.getBrushes().stream().filter(brush1 -> brush1.getConsumerTag().equals(brush.getConsumerTag())).toList();
            if (!brushLive.isEmpty()){
                brushLive.get(0).updatePosition(brush.getX(), brush.getY());
            }else{
                System.out.println("sono nuovo");
                brushManager.addBrush(new BrushManager.Brush(brush.getX(), brush.getY(), brush.getColor(), brush.getConsumerTag()));
                System.out.println("dim: "+brushManager.getBrushes().size());
            }
        });

    }

    public String getConsumerTag() {
        return consumerTag;
    }
}
