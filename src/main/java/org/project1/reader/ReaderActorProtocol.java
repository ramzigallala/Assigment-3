package org.project1.reader;

import java.io.File;

public class ReaderActorProtocol {
    public static class receiveMsg {

        private final File file;

        public receiveMsg(File file) {
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }
}
