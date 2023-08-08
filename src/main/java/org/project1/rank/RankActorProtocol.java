package org.project1.rank;

import akka.actor.ActorRef;

public class RankActorProtocol {
    public static class receiveMsg {

        private final FileEntry fileEntry;

        public receiveMsg(FileEntry fileEntry) {
            this.fileEntry = fileEntry;
        }

        public FileEntry getFileEntry() {
            return fileEntry;
        }
    }
    public static class setNumElements {
        private final long numElements;

        public setNumElements(long numElements) {
            this.numElements = numElements;
        }

        public long getNumElements() {
            return this.numElements;
        }
    }

    public static class startMsg {
        private final ActorRef bootActor;
        private final int[] buckets;
        private final int maxTopFiles;
        private final int bucketSize;

        public startMsg(ActorRef bootActor, int bucketsNum, int maxTopFiles, int bucketSize) {
            this.bootActor = bootActor;
            this.buckets = new int[bucketsNum];;
            this.maxTopFiles = maxTopFiles;
            this.bucketSize = bucketSize;
        }

        public ActorRef getBootActor() {
            return bootActor;
        }

        public int[] getBuckets() {
            return buckets;
        }

        public int getMaxTopFiles() {
            return maxTopFiles;
        }

        public int getBucketSize() {
            return bucketSize;
        }

    }
}
