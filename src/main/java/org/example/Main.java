package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        final File dir = new File("C:\\Users\\mikim\\Desktop\\prova");
        final ActorSystem system = ActorSystem.create("myActorSystem");
        final ActorRef bootActor = system.actorOf(Props.create(BootActor.class), "bootActor");
        bootActor.tell(new BootActor.BootMsg(dir), null);
    }
}
