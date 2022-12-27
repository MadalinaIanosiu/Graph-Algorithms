import java.awt.*;

public class Arc {
    private final Point start;
    private final Point end;
    //public Polygon arrow;

    public Arc(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public void drawArc(Graphics g) {
        if (start != null) {
            g.setColor(Color.BLACK);
            g.drawLine(start.x, start.y, end.x, end.y);
        }
    }

    public void drawDirectedArc(Graphics g){
        g.setColor(Color.BLACK);
        g.drawLine(start.x, start.y, end.x, end.y);

        int dx = end.x - start.x;
        int dy = end.y - start.y;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - 15;
        double xn = xm;
        double ym = 15;
        double yn = - 15, x;
        double sin = dy / D;
        double cos = dx / D;

        x = xm*cos - ym*sin + start.x;
        ym = xm*sin + ym*cos + start.y;
        xm = x;

        x = xn*cos - yn*sin + start.x;
        yn = xn*sin + yn*cos + start.y;
        xn = x;

        int[] xpoints = {end.x, (int) xm, (int) xn};
        int[] ypoints = {end.y, (int) ym, (int) yn};

        Polygon arrow;
        arrow = new Polygon(xpoints, ypoints, 3);
        g.fillPolygon(arrow);
    }

}
