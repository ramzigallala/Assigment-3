package org.project1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.project1.GUI.AnalyserGUI;
import org.project1.GUI.ViewActor;
import org.project1.GUI.ViewActorProtocol;
import org.project1.boot.BootActor;
import org.project1.boot.BootActorProtocol;

import java.io.File;


public class Main {

    static void GUI(){
        new AnalyserGUI();
    }

    static void CLI(int bucketsNumber, int maxTopFiles, int bucketSize, String dir) {
        Config customConf = ConfigFactory.load("reference.conf");
        ActorSystem system = ActorSystem.create("myActorSystem", customConf);
        final ActorRef bootActor = system.actorOf(Props.create(BootActor.class), "bootActor");
        final ActorRef viewActor = system.actorOf(Props.create(ViewActor.class),"viewActor");
        viewActor.tell(new ViewActorProtocol.consoleInfo(100), null);
        bootActor.tell(new BootActorProtocol.BootMsg(bucketsNumber, maxTopFiles, bucketSize, new File(dir), viewActor), null);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            CLI(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
        } else {
            GUI();
        }
    }
}
