package org.project2.rabbit;

import org.project2.rabbit.gui.PixelGridView;
import org.project2.rabbit.message.BrushManager;
import org.project2.rabbit.message.PixelGrid;
import org.project2.rabbit.message.StatusManager;
import org.project2.rabbit.messages_manager.ManagerCommunication;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class PixelArtMain2 {
    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //creo il cursore


        var brushManager = new BrushManager();
        PixelGrid grid = new PixelGrid(40, 40);
        PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);
        ManagerCommunication managerCommunication = new ManagerCommunication(brushManager,view,grid);

        var localBrush = new BrushManager.Brush(0, 0, randomColor(), managerCommunication.getConsumerTag());

        //lo aggiungo alla lista dei cursori
        brushManager.addBrush(localBrush);


        //creo la griglia

        //alcuni quadrati li coloro
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            grid.set(rand.nextInt(40), rand.nextInt(40), randomColor());
        }


        //visualizzo la griglia


        //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
        view.addMouseMovedListener((x, y) -> {
            localBrush.updatePosition(x, y);
            StatusManager statusManager = new StatusManager(grid, brushManager);
            managerCommunication.getClientSender().sendUpdate(statusManager.getStatus());
        });

        //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
        view.addPixelGridEventListener((x, y) -> {
            grid.set(x, y, localBrush.getColor());
            StatusManager statusManager = new StatusManager(grid, brushManager);
            managerCommunication.getClientSender().sendUpdate(statusManager.getStatus());
        });

        //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
        view.addColorChangedListener(color -> {
            localBrush.setColor(color);
            StatusManager statusManager = new StatusManager(grid, brushManager);
            managerCommunication.getClientSender().sendUpdate(statusManager.getStatus());
        });

        view.display();


    }


}
