import java.awt.*;

public class Arc {
    private Point start;
    private Point end;

    public Arc(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public void drawArc(Graphics g) {
        if (start != null) {
            g.setColor(Color.black);
            g.drawLine(start.x, start.y, end.x, end.y);
        }
    }
}

