package org.project1.boot;

import java.io.File;

public class BootActorProtocol {
    public static class BootMsg {
        private final int bucketsNumber;
        private final int maxTopFiles;
        private final int bucketSize;
        private final File dir;

        public BootMsg(int bucketsNumber, int maxTopFiles, int bucketSize, File dir) {
            this.bucketsNumber = bucketsNumber;
            this.maxTopFiles = maxTopFiles;
            this.bucketSize = bucketSize;
            this.dir = dir;
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
