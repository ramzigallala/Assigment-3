package org.project2.ex;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Join;
import org.project2.ex.brushmanager.*;
import org.project2.ex.grid.GridActor;
import org.project2.ex.grid.GridActorProtocols;
import org.project2.ex.manageactors.*;
import org.project2.ex.pixelgridview.PixelGridView;
import org.project2.ex.typo.BootMainProtocols;
import org.project2.ex.utility.CborSerializable;
import org.project2.ex.grid.PixelGrid;

import java.util.Optional;
import java.util.Random;

public class BootMain extends AbstractBehavior<BootMainProtocols.BootMsg> {
    public BootMain(ActorContext<BootMainProtocols.BootMsg> context) {
        super(context);
    }

    @Override
    public Receive<BootMainProtocols.BootMsg> createReceive() {
        return newReceiveBuilder()
                .onMessage(BootMainProtocols.BootMsg.class, this::onBootMsg)
                .build();
    }

    private Behavior<BootMainProtocols.BootMsg> onBootMsg(BootMainProtocols.BootMsg msg) {


        //gestione connessione online
        ActorRef<Receptionist.Listing> listenerActors = this.getContext().spawn(ListenerActors.create(), "listener");
        ActorRef<CborSerializable> masterSenderMsg = this.getContext().spawn(MasterSenderMsg.create(), "masterSenderMsg");
        ActorRef<CborSerializable> masterReceiverMsg = this.getContext().spawn(MasterReceiverMsg.create("masterReceiverMsg2"), "masterReceiverMsg2");

        //accedo al cluster
        Cluster cluster = Cluster.get(getContext().getSystem());
        cluster.manager().tell(Join.create(cluster.selfMember().address()));




        ActorRef<BrushManagerProtocols> brushManager = this.getContext().spawn(BrushManager.create(), "brushManager");
        ActorRef<BrushProtocols> localBrush = this.getContext().spawn(Brush.create(), "localBrush");
        BrushInfo localBrushInfo = new BrushInfo(5,2, randomColor(), "local");
        localBrush.tell(new BrushProtocols.BootMsg(localBrushInfo, Optional.empty(), brushManager));

        PixelGrid grid = new PixelGrid(30,30);
        ActorRef<GridActorProtocols> gridActor = this.getContext().spawn(GridActor.create(), "gridActor");
        gridActor.tell(new GridActorProtocols.Boot(grid));
        PixelGridView view = new PixelGridView(grid, brushManager, 600, 600, this.getContext());
        masterReceiverMsg.tell(new MasterReceiverMsgProtocols.BootMsg(brushManager,view, gridActor));

        //gestiamo la posizione del brush
        view.addMouseMovedListener((x, y) -> {
            localBrush.tell(new BrushProtocols.UpdatePositionMsg(x,y));
            BrushInfo bi = new BrushInfo(x,y, localBrushInfo.getColor(),"outside");
            masterSenderMsg.tell(new MasterSenderMsgProtocols.SendBrush(bi));
            view.refresh();
        });


        //cambiamo il colore del brush
        view.addColorChangedListener(color -> {
            localBrush.tell(new BrushProtocols.UpdateColorMsg(color));
        });

        //cambiamo il colore del pixel
        view.addPixelGridEventListener((x, y) -> {
            //grid.set(x, y, localBrushInfo.getColor());
            masterSenderMsg.tell(new MasterSenderMsgProtocols.SendGrid(grid, System.currentTimeMillis()));
            gridActor.tell(new GridActorProtocols.UpdateCell(x,y,localBrushInfo.getColor()));
            view.refresh();
        });


        view.display();

        return this;
    }

    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }
}

