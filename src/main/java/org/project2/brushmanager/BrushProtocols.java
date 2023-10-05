package org.project2.brushmanager;


import akka.actor.typed.ActorRef;

public interface BrushProtocols {
    public static class UpdatePositionMsg implements BrushProtocols {
        private final int x, y;

        public UpdatePositionMsg(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static class UpdateColorMsg implements BrushProtocols {
        private final int color;

        public UpdateColorMsg(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
    public static class BootMsg implements BrushProtocols {
        private final BrushInfo brushInfo;
        private final ActorRef<BrushManagerProtocols> brushManager;



        public BootMsg(BrushInfo brushInfo, ActorRef<BrushManagerProtocols> brushManager) {
            this.brushInfo=brushInfo;
            this.brushManager = brushManager;
        }

        public BrushInfo getBrushInfo() {
            return brushInfo;
        }

        public ActorRef<BrushManagerProtocols> getBrushManager() {
            return brushManager;
        }


    }


}
