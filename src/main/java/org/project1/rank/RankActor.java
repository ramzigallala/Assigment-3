package org.project1.rank;

import akka.actor.AbstractActor;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

public class RankActor extends AbstractActor {
    private Optional<Long> numElements=Optional.empty();
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RankActorProtocol.setNumElements.class, this::setNumElements)
                //.match(RankActorProtocol.prova2.class, this::p2)
                .build();
    }

    private void setNumElements(RankActorProtocol.setNumElements msg) {
        if(numElements.isEmpty())
            this.numElements=Optional.of(msg.getNumElements());
        System.out.println("rank "+this.numElements.get());
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
