package org.project2.ex.manageactors.ex.visualiserPanel;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.ActorContext;
import org.project2.ex.manageactors.ex.brushmanager.BrushManagerProtocols;
import org.project2.ex.manageactors.ex.grid.PixelGrid;
import org.project2.ex.manageactors.ex.typo.BootMainProtocols;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VisualiserPanel extends JPanel {
    private static final int STROKE_SIZE = 1;
    private final ActorRef<BrushManagerProtocols> brushManager;
    private final PixelGrid grid;
    private final int w,h;

    private final List<BrushDraw> brushDraws = new ArrayList<>();
    private ActorContext<BootMainProtocols.BootMsg> context;


    public VisualiserPanel(PixelGrid grid, ActorRef<BrushManagerProtocols> brushManager, int w, int h){
        setSize(w,h);
        this.grid = grid;
        this.w = w;
        this.h = h;
        this.brushManager = brushManager;
        this.setPreferredSize(new Dimension(w, h));
    }

    public VisualiserPanel(PixelGrid grid, ActorRef<BrushManagerProtocols> brushManager, int w, int h, ActorContext<BootMainProtocols.BootMsg> context) {
        this.context = context;
        setSize(w,h);
        this.grid = grid;
        this.w = w;
        this.h = h;
        this.brushManager = brushManager;
        this.setPreferredSize(new Dimension(w, h));
    }

    public void paint(Graphics g){


        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.clearRect(0, 0, this.getWidth(), this.getHeight());

        int dx = w / grid.getNumColumns();
        int dy = h / grid.getNumRows();
        g2.setStroke(new BasicStroke(STROKE_SIZE));
        for (int i = 0; i < grid.getNumRows(); i++) {
            int y = i * dy;
            g2.drawLine(0, y, w, y);
        }

        for (int i = 0; i < grid.getNumColumns(); i++) {
            int x = i * dx;
            g2.drawLine(x, 0, x, h);
        }

        for (int row = 0; row < grid.getNumRows(); row++) {
            int y = row * dy;
            for (int column = 0; column < grid.getNumColumns(); column++) {
                int x = column * dx;
                int color = grid.get(column, row);
                if (color != 0) {
                    g2.setColor(new Color(color));
                    g2.fillRect(x + STROKE_SIZE, y + STROKE_SIZE, dx - STROKE_SIZE, dy - STROKE_SIZE);
                }
            }
        }

        if(!brushDraws.isEmpty()){
            brushDraws.forEach(brushDraw -> {
                g2.setColor(brushDraw.getColor());
                g2.fill(brushDraw.getCircle());
                g2.setStroke(brushDraw.getStroke());
                g2.setColor(Color.black);
                g2.draw(brushDraw.getCircle());
            });
        }

        //disegna il mouse
        brushManager.tell(new BrushManagerProtocols.DrawMsg(brushDraws));
    }
}
