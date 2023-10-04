package org.project2;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.project2.typo.BootMainProtocols;

public class Main1 {
    public static void main(String[] args) {

        Config configFactory1 = ConfigFactory.parseString("akka.remote.artery.canonical.port=2551").withFallback(ConfigFactory.load("application"));
        //è importante che gli actorsystem abbiano lo stesso nome per collegarsi allo stesso cluster
        final ActorSystem<BootMainProtocols.BootMsg> systemBoot = ActorSystem.create(Behaviors.setup(BootMain::new), "boot", configFactory1);
        systemBoot.tell(new BootMainProtocols.BootMsg());

    }
}
