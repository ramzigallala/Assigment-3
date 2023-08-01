package org.example;

import akka.actor.AbstractActor;
import akka.actor.typed.javadsl.Receive;
import java.io.File;
import java.util.Objects;

public class ExploringActor extends AbstractActor {

    private int dirCounter = 1;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ExploringActorProtocol.receiveMsg.class, this::onReceiveMsg)
                .build();
    }
    private void onReceiveMsg(ExploringActorProtocol.receiveMsg msg){
        this.dirCounter = this.dirCounter - 1;
        File dir = msg.getDir();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                this.dirCounter = this.dirCounter + 1;
                this.getSelf().tell(new ExploringActorProtocol.receiveMsg(file), this.getSelf());
            }
        }
        System.out.println(this.dirCounter);
    }
}
