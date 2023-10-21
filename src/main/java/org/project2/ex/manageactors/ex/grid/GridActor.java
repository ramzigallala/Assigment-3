package org.project2.ex.manageactors.ex.grid;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class GridActor extends AbstractBehavior<GridActorProtocols> {
    private PixelGrid grid;
    public GridActor(ActorContext<GridActorProtocols> context) {
        super(context);
    }

    @Override
    public Receive<GridActorProtocols> createReceive() {
        return newReceiveBuilder()
                .onMessage(GridActorProtocols.Boot.class, this::bootMsg)
                .onMessage(GridActorProtocols.UpdateGrid.class, this::updateMsg)
                .onMessage(GridActorProtocols.UpdateCell.class, this::updateCell)
                .build();
    }

    private Behavior<GridActorProtocols> updateCell(GridActorProtocols.UpdateCell msg) {
        System.out.println("update cell");
        grid.set(msg.getX(), msg.getY(), msg.getColor());
        //System.out.print(grid.get(msg.getX(), msg.getY()));




        return this;
    }

    private Behavior<GridActorProtocols> updateMsg(GridActorProtocols.UpdateGrid msg) {
        //devo confrontare le due griglie e aggiornare i pixel
        System.out.println("grid arrived"+msg.getGrid().getNumColumns());
        //grid.set(5,4, 255*255*255);
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int column = 0; column < grid.getNumColumns(); column++) {


                if(msg.getGrid().get(row,column)!=0){
                    grid.set(row,column,msg.getGrid().get(row,column));
                }

            }
        }

        return this;
    }

    private Behavior<GridActorProtocols> bootMsg(GridActorProtocols.Boot msg) {
        grid=msg.getGrid();
        return this;
    }
    public static Behavior<GridActorProtocols> create() {
        return Behaviors.setup(GridActor::new);
    }

}
