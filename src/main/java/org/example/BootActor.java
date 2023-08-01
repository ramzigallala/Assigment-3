package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.io.File;

public class BootActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(BootActor.BootMsg.class, this::onBoot)
                .build();
    }

    private void onBoot(BootMsg bootMsg) {
        final ActorRef explorerActor = this.getContext().actorOf(Props.create(ExploringActor.class), "ExploringActor");
        explorerActor.tell(new ExploringActorProtocol.receiveMsg(bootMsg.getDir()), this.getSelf());
    }

    public static class BootMsg {
        private final File dir;

        public BootMsg(File dir) {
            this.dir = dir;
        }

        public File getDir() {
            return dir;
        }

    }
}
