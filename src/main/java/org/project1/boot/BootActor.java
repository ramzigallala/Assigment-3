package org.project1.boot;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import org.project1.explorer.ExploringActor;
import org.project1.explorer.ExploringActorProtocol;
import org.project1.rank.RankActor;
import org.project1.reader.ReaderActor;

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
        final ActorRef readerActor = this.getContext().actorOf(Props.create(ReaderActor.class), "ReaderActor");
        final ActorRef rankActor = this.getContext().actorOf(Props.create(RankActor.class), "RankActor");
        explorerActor.tell(new ExploringActorProtocol.startMsg(readerActor,rankActor), this.getSelf());
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
