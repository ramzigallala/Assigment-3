package org.project1.rank;

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

}
