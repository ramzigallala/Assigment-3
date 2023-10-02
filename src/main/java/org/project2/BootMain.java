package org.project2;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import org.project2.brushmanager.Brush;
import org.project2.brushmanager.BrushManager;
import org.project2.brushmanager.BrushManagerProtocols;
import org.project2.brushmanager.BrushProtocols;
import org.project2.pixelgridview.PixelGridView;
import org.project2.typo.BootMainProtocols;

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

        ActorRef<BrushManagerProtocols> brushManager = this.getContext().spawn(BrushManager.create(), "brushManager");
        ActorRef<BrushProtocols> localBrush = this.getContext().spawn(Brush.create(), "localBrush");
        localBrush.tell(new BrushProtocols.BootMsg(5,2,randomColor(), brushManager));

        MessageProtocols.PixelGrid grid = new MessageProtocols.PixelGrid(30,30);

        PixelGridView view = new PixelGridView(grid, brushManager, 600, 600);
        view.addMouseMovedListener((x, y) -> {
            localBrush.tell(new BrushProtocols.UpdatePositionMsg(x,y));
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

