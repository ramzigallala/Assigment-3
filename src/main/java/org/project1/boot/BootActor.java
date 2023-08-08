package org.project1.boot;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import org.project1.explorer.ExploringActor;
import org.project1.explorer.ExploringActorProtocol;
import org.project1.rank.RankActor;
import org.project1.rank.RankActorProtocol;
import org.project1.reader.ReaderActor;

import java.io.File;

public class BootActor extends AbstractActor {
    private int bucketsNum;
    private int maxTopFiles;
    private int bucketSize;
    private File dir;

    private final ActorRef explorerActor = this.getContext().actorOf(Props.create(ExploringActor.class), "ExploringActor");
    private final ActorRef readerActor = this.getContext().actorOf(Props.create(ReaderActor.class), "ReaderActor");
    private final ActorRef rankActor = this.getContext().actorOf(Props.create(RankActor.class), "RankActor");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(BootActorProtocol.BootMsg.class, this::onBoot)
                .match(BootActorProtocol.StartExplorer.class, this::startExplorer)
                .build();
    }

    private void startExplorer(BootActorProtocol.StartExplorer msg) {
        explorerActor.tell(new ExploringActorProtocol.receiveMsg(this.dir), this.getSelf());
    }

    private void onBoot(BootActorProtocol.BootMsg msg) {
        this.bucketsNum = msg.getBucketsNumber();
        this.maxTopFiles = msg.getMaxTopFiles();
        this.bucketSize = msg.getBucketSize();
        this.dir = msg.getDir();
        explorerActor.tell(new ExploringActorProtocol.startMsg(readerActor,rankActor), this.getSelf());
        rankActor.tell(new RankActorProtocol.startMsg(this.getSelf(), this.bucketsNum, this.maxTopFiles, this.bucketSize), this.getSelf());
    }
}
