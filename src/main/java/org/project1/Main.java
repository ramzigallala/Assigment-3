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

    public static void main(String[] args) {
        //new AnalyserGUI();

        Config customConf = ConfigFactory.load("reference.conf");
        ActorSystem system = ActorSystem.create("myActorSystem", customConf);
        final ActorRef bootActor = system.actorOf(Props.create(BootActor.class), "bootActor");
        final ActorRef viewActor = system.actorOf(Props.create(ViewActor.class),"viewActor");
        viewActor.tell(new ViewActorProtocol.consoleInfo(100), null);
        bootActor.tell(new BootActorProtocol.BootMsg(5, 10, 100, new File("D:\\Desktop\\PCD"), viewActor), null);

    }
}
