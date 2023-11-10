package org.project3.comunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommunicationManager extends Remote {
    StartStatus getStartStatus() throws RemoteException;
    Status getStatus() throws RemoteException;
    void updatePosition(int x, int y, int id) throws RemoteException;
}
