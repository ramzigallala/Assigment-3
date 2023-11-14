package org.project3;

import org.project3.comunication.PixelGrid;
import org.project3.gui.PixelGridView;

import java.util.Random;

public class PixelArtMain {

	public static int randomColor() {
		Random rand = new Random();
		return rand.nextInt(256 * 256 * 256);
	}

	public static void main(String[] args) {
		ClientPixelArtMain clientPixelArtMain = new ClientPixelArtMain(null);

		//creo il cursore
		var brushManager = clientPixelArtMain.getBrushManager();
		//creo la griglia
		PixelGrid grid = clientPixelArtMain.getGrid();

		//visualizzo la griglia
		PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);
		clientPixelArtMain.startGetUpdate(view);
		//aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
		view.addMouseMovedListener(clientPixelArtMain::updatePosition);

		//aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
		view.addPixelGridEventListener(clientPixelArtMain::updateGrid);

		//aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
		view.addColorChangedListener(clientPixelArtMain::updateColor);

		view.display();
	}

}
