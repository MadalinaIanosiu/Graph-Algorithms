//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Arc {
    public Point start;
    public Point end;

    public Arc(Point start, Point end) {
        this.start = start;
        this.end = end;
    }


    public void drawArc(Graphics g,Color color) {
        if (start != null) {
            g.setColor(color);
            g.drawLine(start.x, start.y, end.x, end.y);
            if (Graf.type=="Componente tari conexe") {
                double angle = Math.atan2(end.y - start.y, end.x - start.x);
                int arrowHeight = 15;
                int halfArrowWidth = 7;
                Point aroBase = new Point(0, 0);
                aroBase.x = (int) (end.x - arrowHeight * Math.cos(angle));
                aroBase.y = (int) (end.y - arrowHeight * Math.sin(angle));
                Point varf1 = new Point(0, 0), varf2 = new Point(0, 0);
                varf1.x = (int) (aroBase.x - halfArrowWidth * Math.cos(angle - Math.PI / 2));
                varf1.y = (int) (aroBase.y - halfArrowWidth * Math.sin(angle - Math.PI / 2));
                varf2.x = (int) (aroBase.x + halfArrowWidth * Math.cos(angle - Math.PI / 2));
                varf2.y = (int) (aroBase.y + halfArrowWidth * Math.sin(angle - Math.PI / 2));
                int deltaX = start.x - end.x;
                int deltaY = start.y - end.y;
                int x[] = { end.x + (int) (deltaX * 0.05), varf1.x + (int) (deltaX * 0.05),
                        varf2.x + (int) (deltaX * 0.05) };
                int y[] = { end.y + (int) (deltaY * 0.05), varf1.y + (int) (deltaY * 0.05),
                        varf2.y + (int) (deltaY * 0.05) };
                int npoints = x.length;// or y.length
                g.drawPolygon(x, y, npoints);// draws polygon outline
                g.fillPolygon(x, y, npoints);// paints a polygon
            }
        }
    }
}
