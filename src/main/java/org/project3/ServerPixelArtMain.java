package org.project3;

import org.project3.comunication.CommunicationManager;
import org.project3.comunication.CommunicationManagerImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerPixelArtMain {
    public static void main(String[] args) {
        try {
            //HelloService helloObj = new HelloServiceImpl();
            //HelloService helloObjStub = (HelloService) UnicastRemoteObject.exportObject(helloObj, 0);

            //Counter count = new CounterImpl(0);
            //Counter countStub = (Counter) UnicastRemoteObject.exportObject(count, 0);
            CommunicationManager communicationManager = new CommunicationManagerImpl();
            CommunicationManager communicationManagerStub = (CommunicationManager) UnicastRemoteObject.exportObject(communicationManager, 0);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();

            //registry.rebind("helloObj", helloObjStub);
            //registry.rebind("countObj", countStub);
            registry.rebind("communicationManager", communicationManagerStub);

            System.out.println("Objects registered.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
