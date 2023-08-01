package org.example;
import java.io.File;

public class ExploringActorProtocol {
    public static class receiveMsg {

        private final File directory;

        public receiveMsg(File directory) {
            this.directory = directory;
        }

        public File getDir() {
            return directory;
        }
    }
}
