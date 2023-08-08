package org.project1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.project1.boot.BootActor;
import org.project1.boot.BootActorProtocol;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        final File dir = new File("C:\\Users\\mikim\\Desktop");
        //final File dir = new File("D:\\Desktop\\PCD\\prova");
        //final File dir = new File("D:\\Desktop\\PCD\\TestFolder2");
        final ActorSystem system = ActorSystem.create("myActorSystem");
        final ActorRef bootActor = system.actorOf(Props.create(BootActor.class), "bootActor");
        bootActor.tell(new BootActorProtocol.BootMsg(5, 10, 1_000, dir), null);
    }
}
