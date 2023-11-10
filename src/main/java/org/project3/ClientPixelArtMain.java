package org.project3;

import org.project3.comunication.*;
import org.project3.gui.PixelGridView;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
            //Counter c = (Counter) registry.lookup("countObj");
            communicationManager = (CommunicationManager) registry.lookup("communicationManager");
            StartStatus startStatus = communicationManager.getStartStatus();
            id=startStatus.id();
            brushManager=startStatus.brushManager();
            grid=startStatus.grid();
            System.out.println("ID: "+id);
            /*
            int value = c.getValue();
            System.out.println("> value "+value);
            c.inc();
            System.out.println("> value "+c.getValue());
            */
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
        Timer timer = new Timer();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                Status status = communicationManager.getStatus();
                updateBrushManager(status);
                grid = status.grid();
                view.refresh();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        },0, 10, TimeUnit.MILLISECONDS);



    }

    private void updateBrushManager(Status status) {
        status.brushManager().getBrushes().forEach(brush -> {
            if(brushManager.getBrushes().stream().anyMatch(b -> b.getId()== brush.getId())){
                brushManager.getBrushes().get(brush.getId()).updatePosition(brush.getX(), brush.getY());
            }else{
                brushManager.addBrush(brush);
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

}
