package org.project1.rank;

import akka.actor.AbstractActor;
import org.project1.boot.BootActorProtocol;
import org.project1.explorer.ExploringActorProtocol;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

public class RankActor extends AbstractActor {

    private final TreeSet<FileEntry> files = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
    private int[] buckets;
    private int maxTopFiles;
    private int bucketSize;
    private Optional<Long> numElements=Optional.empty();
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
       // System.out.println(this.files.stream().toList());
        Arrays.stream(this.buckets).asLongStream().forEach(System.out::println);
    }


//sbagliata la gestione del costruttore, fa bloccare tutto. Bisogna gestire il tutto tramite i messaggi oppure passare a costruttore il context e fare il super
   /* private final TreeSet<FileEntry> files = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
    private final int[] buckets;
    private final int maxTopFiles;
    private final int bucketSize;

    public RankActor(int[] buckets, int maxTopFiles, int bucketSize) {
        this.buckets = buckets;
        this.maxTopFiles = maxTopFiles;
        this.bucketSize = bucketSize;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RankActorProtocol.receiveMsg.class, this::onReceiveMsg)
                .build();
    }

    private void onReceiveMsg(RankActorProtocol.receiveMsg msg) {

        FileEntry fileEntry = msg.getFileEntry();
        this.files.add(fileEntry);
        if (this.files.size() > this.maxTopFiles)
            this.files.remove(this.files.last());
        int bucketIdx = (int)Math.min((msg.getFileEntry().lines()) / this.bucketSize, this.buckets.length-1);
        this.buckets[bucketIdx]++;
        System.out.println(this.maxTopFiles);


    }
*/

}
