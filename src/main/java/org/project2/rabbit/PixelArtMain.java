package org.project2.rabbit;

import java.util.Random;

public class PixelArtMain {

	public static int randomColor() {
		Random rand = new Random();
		return rand.nextInt(256 * 256 * 256);
	}

	public static void main(String[] args) {
		//creo il cursore
		var brushManager = new BrushManager();
		var localBrush = new BrushManager.Brush(0, 0, randomColor());
		//var fooBrush = new BrushManager.Brush(0, 0, randomColor());
		//lo aggiungo alla lista dei cursori
		brushManager.addBrush(localBrush);
		//brushManager.addBrush(fooBrush);
		//creo la griglia
		PixelGrid grid = new PixelGrid(40,40);
		//alcuni quadrati li coloro
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			grid.set(rand.nextInt(40), rand.nextInt(40), randomColor());
		}
		//visualizzo la griglia
		PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);

		//aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
		view.addMouseMovedListener((x, y) -> {
			localBrush.updatePosition(x, y);
			view.refresh();
		});

		//aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
		view.addPixelGridEventListener((x, y) -> {
			grid.set(x, y, localBrush.getColor());
			view.refresh();
		});

		//aggiungo un listener per fare l'update appena viene triggerato il listener in pixelGridView
		view.addColorChangedListener(localBrush::setColor);

		view.display();
	}

}
