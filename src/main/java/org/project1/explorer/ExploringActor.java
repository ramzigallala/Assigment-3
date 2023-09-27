package org.project1.explorer;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import org.project1.rank.RankActorProtocol;
import org.project1.reader.ReaderActorProtocol;

import java.io.File;
import java.util.Objects;

public class ExploringActor extends AbstractActor {

    private int dirCounter = 1;
    private long numFiles=0;
    private ActorRef readerActor;
    private ActorRef rankActor;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ExploringActorProtocol.startMsg.class, this::startMsg)
                .build();
    }

    private void startMsg(ExploringActorProtocol.startMsg msg) {
        readerActor= msg.getReaderActor();
        rankActor = msg.getRankActor();
        this.getContext().become(receiverDirectory());
    }

    public Receive receiverDirectory(){
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
            } else if (file.getName().endsWith(".java")) {
                //System.out.println(file.getPath());
                this.numFiles= this.numFiles+1;
                readerActor.tell(new ReaderActorProtocol.startMsg(rankActor), this.getSelf());
                readerActor.tell(new ReaderActorProtocol.receiveMsg(file), this.getSelf());
            }
        }
        checkEnd();
        //System.out.println(this.dirCounter);
    }

    private void checkEnd(){
        if(this.dirCounter==0){
            //System.out.println("finito, numero file: "+this.numFiles);
            rankActor.tell(new RankActorProtocol.setNumElements(this.numFiles),this.getSelf());
        }
    }
}
