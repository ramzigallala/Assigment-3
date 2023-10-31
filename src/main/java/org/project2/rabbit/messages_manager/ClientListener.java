package org.project2.rabbit.messages_manager;

import com.rabbitmq.client.*;
import org.apache.commons.lang3.SerializationUtils;
import org.project2.rabbit.message.BrushManager;
import org.project2.rabbit.message.PixelGrid;
import org.project2.rabbit.gui.PixelGridView;
import org.project2.rabbit.message.StatusManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ClientListener {
    private static final String EXCHANGE_NAME = "direct_messages";
    private final BrushManager brushManager;
    private final PixelGridView view;
    private final PixelGrid grid;
    private final String queueName;
    private Channel channel;


    public ClientListener(BrushManager brushManager, PixelGridView view, PixelGrid grid, Channel channel) throws IOException, TimeoutException {
        this.brushManager = brushManager;
        this.view=view;
        this.grid=grid;
        this.channel = channel;

        queueName = channel.queueDeclare().getQueue();
        //first = checkExchange(connection);
        channel.queueBind(queueName, EXCHANGE_NAME, "start");
        channel.queueBind(queueName, EXCHANGE_NAME, "update");
        channel.queueBind(queueName, EXCHANGE_NAME, "exit");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    }
    public String setBasicConsume(DeliverCallback callback) throws IOException {
        return channel.basicConsume(queueName, false, callback, consumerTag -> {});
    }


}
