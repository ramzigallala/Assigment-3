package org.project3;

import org.project3.comunication.*;
import org.project3.gui.PixelGridView;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.*;

public class ClientPixelArtMain {

    private CommunicationManager communicationManager;
    private int id;
    private BrushManager brushManager;
    private PixelGrid grid;

    public ClientPixelArtMain(String host) {

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            communicationManager = (CommunicationManager) registry.lookup("communicationManager");
            StartStatus startStatus = communicationManager.getStartStatus();
            id=startStatus.id();
            brushManager=startStatus.brushManager();
            grid=startStatus.grid();
            System.out.println("ID: "+id);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public BrushManager getBrushManager() {
        return brushManager;
    }

    public PixelGrid getGrid() {
        return grid;
    }
    public void startGetUpdate(PixelGridView view){
        view.addWindowListener(closeConnection());
        int cores = Runtime.getRuntime().availableProcessors()+1;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(cores);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                Status status = communicationManager.getStatus();
                updateBrushManager(status);
                updateGrid(status);
                view.refresh();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        },0, 60, TimeUnit.MILLISECONDS);

    }

    private void updateGrid(Status status) {
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int column = 0; column < grid.getNumColumns(); column++) {
                grid.set(row,column, status.grid().get(row,column));
            }
        }
    }

    private void updateBrushManager(Status status) {
        brushManager.getBrushes().removeIf(brushOfClient -> status.brushManager().getBrushes().stream().noneMatch(brushOfServer -> brushOfClient.getId()==brushOfServer.getId()));
        status.brushManager().getBrushes().forEach(brushOfServer -> {
                Optional<BrushManager.Brush> brushOfClient = getBrush(brushOfServer.getId());
                brushOfClient.ifPresent(value -> {
                    value.updatePosition(brushOfServer.getX(), brushOfServer.getY());
                    value.setColor(brushOfServer.getColor());
                });
                if(brushOfClient.isEmpty()){
                    brushManager.addBrush(brushOfServer);
                }

        });
    }

    public void updatePosition(int x, int y){
        try {
            communicationManager.updatePosition(x, y, id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateColor(int color){
        try {
            communicationManager.updateColor(color, id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGrid(int x, int y){
        try {
            communicationManager.updateGrid(x, y, id);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private WindowListener closeConnection() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    communicationManager.removeBrush(id);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }
    private Optional<BrushManager.Brush> getBrush(int id){
        return brushManager.getBrushes().stream()
                .filter(brush -> brush.getId()==id)
                .findFirst();
    }
}
