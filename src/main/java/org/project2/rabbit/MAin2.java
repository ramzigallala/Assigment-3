package org.project2.rabbit;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class MAin2 {
    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }

    public static void main(String[] args) {
        //creo il cursore


        try {
            var brushManager = new BrushManager();
            PixelGrid grid = new PixelGrid(40,40);
            PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);
            ClientListener clientListener = new ClientListener(brushManager, view);
            ClientSender clientSender = new ClientSender();

            var localBrush = new BrushManager.Brush(0, 0, randomColor(), clientListener.getConsumerTag());
            //var fooBrush = new BrushManager.Brush(0, 0, randomColor());
            //lo aggiungo alla lista dei cursori
            brushManager.addBrush(localBrush);
            //brushManager.addBrush(fooBrush);
            //creo la griglia
            //alcuni quadrati li coloro
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                grid.set(rand.nextInt(40), rand.nextInt(40), randomColor());
            }
            //visualizzo la griglia


            //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
            view.addMouseMovedListener((x, y) -> {
                //System.out.println();
                localBrush.updatePosition(x, y);
                //System.out.println("mioMain "+brushManager.getBrushes().get(0).getX());
                //System.out.println("mioMain "+brushManager.getBrushes().get(0).getX());
                StatusManager statusManager = new StatusManager(grid,brushManager);
                clientSender.sendUpdate(statusManager.getStatus());
                //view.refresh();
            });

            //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
            view.addPixelGridEventListener((x, y) -> {
                grid.set(x, y, localBrush.getColor());
                view.refresh();
            });

            //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
            view.addColorChangedListener(localBrush::setColor);

            view.display();
        } catch (IOException | TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
