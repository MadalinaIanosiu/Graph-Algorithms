import java.awt.Color;
import java.awt.Graphics;

public class Arc {
    private Node start;
    private Node end;

    public Arc(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public void drawArc(Graphics g) {
        if (start != null) {
            g.setColor(Color.red);
            g.drawLine(start.getCoordX()+15, start.getCoordY()+15, end.getCoordX()+15, end.getCoordY()+15);
        }
    }

    public void drawArrow(Graphics g) {
        if (start != null) {
            int heightArrow = 20;
            int widthArrow = 5;
            int xDifference = end.getCoordX() - start.getCoordX();
            int yDifference = end.getCoordY() - start.getCoordY();
            double D = Math.sqrt(xDifference * xDifference + yDifference * yDifference);
            double xm = D - heightArrow,
                    xn = xm,
                    ym = widthArrow,
                    yn = -widthArrow,
                    x;
            double sin = yDifference / D,
                    cos = xDifference / D;

            x = xm * cos - ym * sin + start.getCoordX();
            ym = xm * sin + ym * cos + start.getCoordY();
            xm = x;

            x = xn * cos - yn * sin + start.getCoordX();
            yn = xn * sin + yn * cos + start.getCoordY();
            xn = x;

            int[] xpoints = {end.getCoordX(), (int) xm, (int) xn};
            int[] ypoints = {end.getCoordY(), (int) ym, (int) yn};

            g.setColor(Color.RED);
            g.drawLine(start.getCoordX(), start.getCoordY(), end.getCoordX(), end.getCoordY());
            g.fillPolygon(xpoints, ypoints, 3);
        }
    }
}

