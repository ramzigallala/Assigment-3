package org.project2;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Join;
import org.project2.brushmanager.*;
import org.project2.manageactors.MasterSenderMsg;
import org.project2.manageactors.MasterSenderMsgProtocols;
import org.project2.manageactors.ListenerActors;
import org.project2.pixelgridview.PixelGridView;
import org.project2.typo.BootMainProtocols;
import org.project2.utility.CborSerializable;
import org.project2.visualiserPanel.PixelGrid;

import java.time.Duration;
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
        //accedo al cluster
        Cluster cluster = Cluster.get(getContext().getSystem());
        cluster.manager().tell(Join.create(cluster.selfMember().address()));




        ActorRef<BrushManagerProtocols> brushManager = this.getContext().spawn(BrushManager.create(), "brushManager");
        ActorRef<BrushProtocols> localBrush = this.getContext().spawn(Brush.create(), "localBrush");
        BrushInfo localBrushInfo = new BrushInfo(5,2, randomColor());
        localBrush.tell(new BrushProtocols.BootMsg(localBrushInfo, brushManager));

        PixelGrid grid = new PixelGrid(30,30);

        PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);
        //gestiamo la posizione del brush
        view.addMouseMovedListener((x, y) -> {
            localBrush.tell(new BrushProtocols.UpdatePositionMsg(x,y));
            BrushInfo bi = new BrushInfo(x,y, randomColor());
            masterSenderMsg.tell(new MasterSenderMsgProtocols.SendBrush(bi));
            view.refresh();
        });

        /*
        //cambiamo il colore del brush
        view.addColorChangedListener(color -> {
            localBrush.tell(new BrushProtocols.UpdateColorMsg(color));
        });

        //cambiamo il colore del pixel
        view.addPixelGridEventListener((x, y) -> {
            grid.set(x, y, localBrushInfo.getColor());
            view.refresh();
        });

         */
        view.display();

        return this;
    }

    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }
}

