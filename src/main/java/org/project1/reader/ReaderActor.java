package org.project1.reader;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ReaderActor extends AbstractActor {
    private ActorRef rankActor;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReaderActorProtocol.startMsg.class, this::startMsg)
                .build();
    }

    private void startMsg(ReaderActorProtocol.startMsg msg) {
        rankActor = msg.getRankActor();
        this.getContext().become(receiverMsg());
    }

    public Receive receiverMsg(){
        return receiveBuilder()
                .match(ReaderActorProtocol.receiveMsg.class, this::onReceiveMsg)
                .build();
    }

    private void onReceiveMsg(ReaderActorProtocol.receiveMsg msg){
        try {
            File file = msg.getFile();
            String nameFile= file.getAbsolutePath();
            Long numLines = Files.lines(file.toPath()).count();
            //System.out.println(nameFile+" "+numLines);
            //TODO fare o un messaggio di start oppure tramite costruttore ma con le giuste accortezze
            //rankActor.tell(new RankActorProtocol.receiveMsg(file), this.getSelf());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
