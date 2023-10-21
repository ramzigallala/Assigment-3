package org.project2.ex.manageactors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import org.project2.ex.brushmanager.Brush;
import org.project2.ex.brushmanager.BrushManagerProtocols;
import org.project2.ex.brushmanager.BrushProtocols;
import org.project2.ex.grid.GridActorProtocols;
import org.project2.ex.grid.PixelGrid;
import org.project2.ex.pixelgridview.PixelGridView;
import org.project2.ex.utility.CborSerializable;

import java.util.Optional;

public class MasterReceiverMsg extends AbstractBehavior<CborSerializable> {

    private ActorRef<BrushManagerProtocols> brushManager;
    private PixelGridView pixelGridView;
    private ActorRef<GridActorProtocols> gridActor;
    public MasterReceiverMsg(ActorContext<CborSerializable> context) {
        super(context);
    }

    @Override
    public Receive<CborSerializable> createReceive() {
        return newReceiveBuilder()
                .onMessage(MasterReceiverMsgProtocols.BootMsg.class, this::onBootMsg)
                .onMessage(MasterReceiverMsgProtocols.SentBrush.class, this::brushInfo)
                .onMessage(MasterReceiverMsgProtocols.SentGrid.class, this::gridSet)
                .build();
    }

    private Behavior<CborSerializable> brushInfo(MasterReceiverMsgProtocols.SentBrush msg) {

        if(brushManager!=null && pixelGridView!=null){
            //System.out.println("message arrived "+msg.getBrush().getX()+" "+msg.getBrush().getY()+ " "+msg.getBrush().getRole());

            ActorRef<BrushProtocols> brushSent = this.getContext().spawnAnonymous(Brush.create());
            brushSent.tell(new BrushProtocols.BootMsg(msg.getBrush(), Optional.of(msg.getName()), brushManager));
            //System.out.println(brushManager);
            pixelGridView.refresh();
        }
        return this;
    }

    private Behavior<CborSerializable> onBootMsg(MasterReceiverMsgProtocols.BootMsg msg) {
        brushManager= msg.getBrushManager();
        pixelGridView=msg.getPixelGridView();
        gridActor = msg.getGridActor();
        return this;
    }

    private Behavior<CborSerializable> gridSet(MasterReceiverMsgProtocols.SentGrid msg) {
        if(gridActor!=null){
            System.out.println("Receiver: time grid sent: "+msg.getTime());
            PixelGrid tmp = msg.getGrid();
            gridActor.tell(new GridActorProtocols.UpdateGrid(tmp));
            pixelGridView.refresh();
        }

        return this;
    }

    public static Behavior<CborSerializable> create(String name) {

        return Behaviors.setup(contex -> {
            contex.getSystem()
                    .receptionist()
                    .tell(Receptionist.register(ServiceKey.create(CborSerializable.class,"tunnel"),contex.getSelf()));
            System.out.println("Registered Actor: "+name);
            return new MasterReceiverMsg(contex).createReceive();
        });
    }
}
