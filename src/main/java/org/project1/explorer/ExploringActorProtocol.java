package org.project1.explorer;
import akka.actor.ActorRef;

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

    public static class startMsg {

        private final ActorRef readerActor;
        private final ActorRef rankActor;

        public startMsg(ActorRef readerActor, ActorRef rankActor) {
            this.readerActor = readerActor;
            this.rankActor = rankActor;
        }

        public ActorRef getReaderActor() {
            return this.readerActor;
        }

        public ActorRef getRankActor() {
            return this.rankActor;
        }
    }
}
