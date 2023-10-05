package org.project2.manageactors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import org.project2.utility.CborSerializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MasterSenderMsg extends AbstractBehavior<CborSerializable> {
    private HashMap<String, ActorRef<CborSerializable>> masterReceivers =null;
    public MasterSenderMsg(ActorContext<CborSerializable> context) {
        super(context);
    }

    @Override
    public Receive<CborSerializable> createReceive() {
        return newReceiveBuilder()
                .onMessage(MasterSenderMsgProtocols.actorsMsg.class, this::onBootMsg)
                .onMessage(MasterSenderMsgProtocols.SendBrush.class, this::sendInfo)
                .build();
    }

    private Behavior<CborSerializable> sendInfo(MasterSenderMsgProtocols.SendBrush msg) {
        if(masterReceivers !=null){
            //System.out.println("sendInfo "+actors.size());

            masterReceivers.forEach((name, actor) -> {

                //System.out.println("onboot information: "+brush.getX());
                actor.tell(new MasterReceiverMsgProtocols.SentBrush(msg.getBrushInfo(), name));
                //System.out.println("send message: "+java.time.LocalDateTime.now());


            });
        }
        return this;
    }

    private Behavior<CborSerializable> onBootMsg(MasterSenderMsgProtocols.actorsMsg msg) {
        masterReceivers = new HashMap<>();
        //masterReceivers.addAll(msg.getMasterReceivers());
        msg.getMasterReceivers().forEach(actor -> {

            if(actor.path().address().toString().contains("@")){
                System.out.println(" added masterReceiver: "+ actor.path());
                //masterReceivers.remove(actor);
                masterReceivers.put(actor.path().address().toString(),actor);
            }


        });
        //System.out.println("size: "+actors.size());
        return this;
    }

    public static Behavior<CborSerializable> create() {

        return Behaviors.setup(contex -> {
            contex.getSystem()
                    .receptionist()
                    .tell(Receptionist.register(ServiceKey.create(CborSerializable.class,"tunnel"),contex.getSelf()));
            //System.out.println("Registered Actor: "+name);
            return new MasterSenderMsg(contex).createReceive();
        });
    }
}
