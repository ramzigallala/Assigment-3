package org.project1.reader;

import akka.actor.ActorRef;

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

    public static class startMsg {
        private final ActorRef rankActor;

        public startMsg(ActorRef rankActor) {
            this.rankActor = rankActor;
        }

        public ActorRef getRankActor() {
            return this.rankActor;
        }
    }
}
