package org.project1.reader;

import akka.actor.AbstractActor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ReaderActor extends AbstractActor {
    //TODO fare il messaggio di start per prendere il riferimento all'attore classifica, solo la prima volta
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReaderActorProtocol.receiveMsg.class, this::onReceiveMsg)
                .build();
    }

    private void onReceiveMsg(ReaderActorProtocol.receiveMsg msg ) {
        try {
            File file = msg.getFile();
            String nameFile= file.getAbsolutePath();
            Long numLines = Files.lines(file.toPath()).count();
            System.out.println(nameFile+" "+numLines);
            //TODO inviare dati all'attore rank
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
