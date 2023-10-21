package org.project2.ex.typo;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Main2 {
    public static void main(String[] args) {
        Config configFactory = ConfigFactory.parseString("akka.remote.artery.canonical.port=2552").withFallback(ConfigFactory.load("application"));
        final ActorSystem<BootMainProtocols.BootMsg> systemBoot = ActorSystem.create(Behaviors.setup(BootMain2::new), "boot",configFactory);
        systemBoot.tell(new BootMainProtocols.BootMsg());

    }
}
