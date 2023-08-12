package org.project1;

import GUI.AnalyserGUI;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.project1.boot.BootActor;
import org.project1.boot.BootActorProtocol;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        new AnalyserGUI();
    }
}
