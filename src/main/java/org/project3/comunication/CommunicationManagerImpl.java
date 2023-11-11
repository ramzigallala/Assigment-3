package org.project3.comunication;

import java.rmi.RemoteException;
import java.util.Random;

import static org.project3.PixelArtMain.randomColor;

public class CommunicationManagerImpl implements CommunicationManager{
    private BrushManager brushManager;
    private final PixelGrid grid;
    private int idDistributor=-1;

    public CommunicationManagerImpl() {
        this.brushManager = new BrushManager();
        this.grid = new PixelGrid(40,40);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            grid.set(rand.nextInt(40), rand.nextInt(40), randomColor());
        }
    }

    @Override
    public synchronized StartStatus  getStartStatus() throws RemoteException {
        idDistributor++;
        var localBrush = new BrushManager.Brush(0, 0, randomColor(), idDistributor);
        brushManager.addBrush(localBrush);
        return new StartStatus(brushManager,grid,idDistributor);
    }

    @Override
    public synchronized Status getStatus() throws RemoteException {
        return new Status(brushManager, grid);
    }

    @Override
    public synchronized void updatePosition(int x, int y, int id) throws RemoteException {
        brushManager.getBrushes().get(id).updatePosition(x, y);
    }

    @Override
    public synchronized void updateColor(int color, int id) throws RemoteException {
        brushManager.getBrushes().get(id).setColor(color);
    }

    @Override
    public synchronized void updateGrid(int x, int y, int id) throws RemoteException {
        int color = brushManager.getBrushes().get(id).getColor();
        grid.set(x,y,color);

    }
}
