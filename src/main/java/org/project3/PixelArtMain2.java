package org.project3;

import org.project3.comunication.PixelGrid;
import org.project3.gui.PixelGridView;

import java.util.Random;

public class PixelArtMain2 {
    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }

    public static void main(String[] args) {
        ClientPixelArtMain clientPixelArtMain = new ClientPixelArtMain(null);

        //creo il cursore
        var brushManager = clientPixelArtMain.getBrushManager();
        //var localBrush = new BrushManager.Brush(0, 0, randomColor(), id);
        //var fooBrush = new BrushManager.Brush(0, 0, randomColor());
        //lo aggiungo alla lista dei cursori
        //brushManager.addBrush(localBrush);
        //brushManager.addBrush(fooBrush);
        //creo la griglia
        PixelGrid grid = clientPixelArtMain.getGrid();
        //alcuni quadrati li coloro
		/*
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			grid.set(rand.nextInt(40), rand.nextInt(40), randomColor());
		}
		*/

        //visualizzo la griglia
        PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);
        clientPixelArtMain.startGetUpdate(view);
        //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
        //localBrush.updatePosition(x, y);
        //view.refresh();
        //System.out.println(x);
        view.addMouseMovedListener(clientPixelArtMain::updatePosition);

        //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
        //grid.set(x, y, localBrush.getColor());
        //view.refresh();
        view.addPixelGridEventListener(clientPixelArtMain::updateGrid);

        //aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
        view.addColorChangedListener(clientPixelArtMain::updateColor);

        view.display();
    }
}
