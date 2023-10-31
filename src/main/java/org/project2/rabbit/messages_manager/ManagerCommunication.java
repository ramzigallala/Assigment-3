package org.project2.rabbit.messages_manager;

import com.rabbitmq.client.*;
import org.apache.commons.lang3.SerializationUtils;
import org.project2.rabbit.gui.PixelGridView;
import org.project2.rabbit.message.BrushManager;
import org.project2.rabbit.message.PixelGrid;
import org.project2.rabbit.message.StatusManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ManagerCommunication {
    private final BrushManager brushManager;
    private final PixelGridView view;
    private final PixelGrid grid;

    private final ClientListener clientListener;
    private final ClientSender clientSender;
    private static final String EXCHANGE_NAME = "direct_messages";
    private final boolean second;
    private final Connection connection;
    private Channel channel;
    private boolean firstModification=false;
    private final String consumerTag;

    public ManagerCommunication(BrushManager brushManager, PixelGridView view, PixelGrid grid) throws IOException, TimeoutException, InterruptedException {
        this.brushManager = brushManager;
        this.view = view;
        this.grid = grid;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.confirmSelect();
        channel.waitForConfirmsOrDie(1000);
        second = checkExchange(connection);

        channel.exchangeDeclare(EXCHANGE_NAME, "direct",false,true, null);

        clientListener = new ClientListener(brushManager, view, grid, channel);
        this.consumerTag= clientListener.setBasicConsume(setCallback());
        clientSender = new ClientSender(channel,consumerTag);
        if(second){
            clientSender.requestStart();
            System.out.println("richiedo start");
        }else{
            firstModification=true;
        }

        this.view.addWindowListener(closeConnection());

    }

    private WindowListener closeConnection() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    System.out.println("ciao");
                    clientSender.sendExit(consumerTag);
                    channel.close();
                    connection.close();
                } catch (IOException | TimeoutException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    private boolean checkExchange(Connection connection) throws IOException {
        try {
            AMQP.Exchange.DeclareOk checkExchange = channel.exchangeDeclarePassive(EXCHANGE_NAME);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            channel = connection.createChannel();
            System.out.println("sono il primo");
            return false;

        }
        return true;
    }

    private DeliverCallback setCallback(){
        return (consumerTagListener, deliveredMessage) -> {
            System.out.println(" [x] Received '" + deliveredMessage.getEnvelope().getRoutingKey() + "':'" + "arrivato" + "'"+deliveredMessage.getProperties().getMessageId()+" bb "+consumerTag);
            if(deliveredMessage.getEnvelope().getRoutingKey().equals("update")){
                updateView(deliveredMessage.getBody());

            }
            if(deliveredMessage.getEnvelope().getRoutingKey().equals("start")){
                StatusManager statusManager = SerializationUtils.deserialize(deliveredMessage.getBody());
                //System.out.println(consumerTagSender+" // "+consumerTag);

                if(firstModification){
                    if(!deliveredMessage.getProperties().getMessageId().equals(consumerTag) && statusManager.isRequestStart()){

                        clientSender.requestStart(new StatusManager(grid, brushManager));
                        //System.out.println("invio start");

                    }
                }else{
                    if(!deliveredMessage.getProperties().getMessageId().equals(consumerTag)){
                        //System.out.println("ricevo start");
                        firstModification=true;
                        updateView(deliveredMessage.getBody());
                    }

                }
            }

            if(deliveredMessage.getEnvelope().getRoutingKey().equals("exit")){
                String consumerTagToRemove = new String(deliveredMessage.getBody(), "UTF-8");
                brushManager.removeBrush(consumerTagToRemove);
            }
            view.refresh();

        };
    }

    private void updateView(byte[] message){

        StatusManager statusManager = SerializationUtils.deserialize(message);

        PixelGrid gridDelivered = statusManager.getGrid();
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int column = 0; column < grid.getNumColumns(); column++) {
                grid.getGrid()[row][column]=gridDelivered.getGrid()[row][column];

            }
        }


        BrushManager brushManagerDelivered = statusManager.getBrushManager();
        brushManagerDelivered.getBrushes().forEach(brush -> {
            List<BrushManager.Brush> brushLive = brushManager.getBrushes().stream().filter(brush1 -> brush1.getConsumerTag().equals(brush.getConsumerTag())).toList();
            if (!brushLive.isEmpty()){
                brushLive.get(0).updatePosition(brush.getX(), brush.getY());
                brushLive.get(0).setColor(brush.getColor());
                //System.out.println("modifico: "+brushLive.get(0).getConsumerTag()+brush.getX()+ brush.getY());
            }else{
                System.out.println("sono nuovo");
                brushManager.addBrush(new BrushManager.Brush(brush.getX(), brush.getY(), brush.getColor(), brush.getConsumerTag()));
                //System.out.println("dim: "+brushManager.getBrushes().size());
            }
        });

    }
    public String getConsumerTag() {
        return consumerTag;
    }

    public ClientSender getClientSender() {
        return clientSender;
    }
}
