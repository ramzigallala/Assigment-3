package org.project1.rank;

import org.project1.GUI.ViewActorProtocol;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import org.project1.boot.BootActorProtocol;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class RankActor extends AbstractActor {

    private final TreeSet<FileEntry> files = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
    private int[] buckets;
    private int maxTopFiles;
    private int bucketSize;
    private Optional<Long> numElements=Optional.empty();
    private ActorRef viewActor;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RankActorProtocol.startMsg.class, this::startMsg)
                .build();
    }

    private void startMsg(RankActorProtocol.startMsg msg) {
        this.getContext().become(receiverMsg());
        this.buckets = msg.getBuckets();
        this.maxTopFiles = msg.getMaxTopFiles();
        this.bucketSize = msg.getBucketSize();
        msg.getBootActor().tell(new BootActorProtocol.StartExplorer(), this.getSelf());
        this.viewActor = msg.getViewActor();
    }

    private Receive receiverMsg() {
        return receiveBuilder()
                .match(RankActorProtocol.setNumElements.class, this::setNumElements)
                .match(RankActorProtocol.receiveMsg.class, this::onReceiveMsg)
                .build();
    }

    private void setNumElements(RankActorProtocol.setNumElements msg) {
        if(numElements.isEmpty())
            this.numElements=Optional.of(msg.getNumElements());
        System.out.println("rank "+this.numElements.get());
    }

    private void onReceiveMsg(RankActorProtocol.receiveMsg msg) {
        FileEntry fileEntry = msg.getFileEntry();
        this.files.add(fileEntry);
        if (this.files.size() > this.maxTopFiles)
            this.files.remove(this.files.last());
        int bucketIdx = (int)Math.min((msg.getFileEntry().lines()) / this.bucketSize, this.buckets.length-1);
        this.buckets[bucketIdx]++;
        this.viewActor.tell(new ViewActorProtocol.receiveResults(this.buckets, files.stream().toList()), this.getSelf());
    }

}
