package org.project1.rank;

import java.io.File;

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
}
