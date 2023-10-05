package org.project2;

import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Join;
import org.project2.brushmanager.*;
import org.project2.manageactors.*;
import org.project2.pixelgridview.PixelGridView;
import org.project2.typo.BootMainProtocols;
import org.project2.utility.CborSerializable;
import org.project2.visualiserPanel.PixelGrid;

import java.util.Optional;
import java.util.Random;

public class BootMain2 extends AbstractBehavior<BootMainProtocols.BootMsg> {
    public BootMain2(ActorContext<BootMainProtocols.BootMsg> context) {
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
        ActorRef<Receptionist.Listing> listenerActors = this.getContext().spawn(ListenerActors.create(), "listener2");
        ActorRef<CborSerializable> masterSenderMsg = this.getContext().spawn(MasterSenderMsg.create(), "masterSenderMsg2");
        ActorRef<CborSerializable> masterReceiverMsg = this.getContext().spawn(MasterReceiverMsg.create("masterReceiverMsg2"), "masterReceiverMsg2");
        //accedo al cluster
        Address seedNodes = AddressFromURIString.parse("akka://boot@127.0.0.1:2551");

        Cluster cluster = Cluster.get(getContext().getSystem());
        cluster.manager().tell(Join.create(seedNodes));




        ActorRef<BrushManagerProtocols> brushManager = this.getContext().spawn(BrushManager.create(), "brushManager");
        ActorRef<BrushProtocols> localBrush = this.getContext().spawn(Brush.create(), "localBrush");
        BrushInfo localBrushInfo = new BrushInfo(5,2, randomColor(), "local");
        localBrush.tell(new BrushProtocols.BootMsg(localBrushInfo, Optional.empty(), brushManager));

        PixelGrid grid = new PixelGrid(30,30);

        PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);
        masterReceiverMsg.tell(new MasterReceiverMsgProtocols.BootMsg(brushManager,view));

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
            grid.set(x, y, localBrushInfo.getColor());
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

