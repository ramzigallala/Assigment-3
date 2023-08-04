package org.project1.rank;

import akka.actor.AbstractActor;

import java.util.Comparator;
import java.util.TreeSet;

public class RankActor extends AbstractActor {

    private final TreeSet<FileEntry> files = new TreeSet<>(Comparator.comparingLong(o -> -o.lines()));
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
    }
}
