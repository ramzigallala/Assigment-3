package org.project1.boot;

import akka.actor.ActorRef;

import java.io.File;

public class BootActorProtocol {
    public static class BootMsg {
        private final int bucketsNumber;
        private final int maxTopFiles;
        private final int bucketSize;
        private final File dir;
        private final ActorRef viewActor;

        public BootMsg(int bucketsNumber, int maxTopFiles, int bucketSize, File dir, ActorRef viewActor) {
            this.bucketsNumber = bucketsNumber;
            this.maxTopFiles = maxTopFiles;
            this.bucketSize = bucketSize;
            this.dir = dir;
            this.viewActor = viewActor;
        }

        public ActorRef getViewActor() {
            return viewActor;
        }

        public int getBucketsNumber() {
            return bucketsNumber;
        }

        public int getMaxTopFiles() {
            return maxTopFiles;
        }

        public int getBucketSize() {
            return bucketSize;
        }

        public File getDir() {
            return dir;
        }
    }

    public static class StartExplorer {
    }
}
