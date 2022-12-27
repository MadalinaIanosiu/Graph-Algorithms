import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Comparator;
class RealArcComparator implements Comparator<Arc>{
    @Override
    public int compare(Arc arc1, Arc arc2) {
        if (arc1.valoare > arc2.valoare)
            return 1;
        else if (arc1.valoare < arc2.valoare)
            return -1;
        return 0;
    }
}
public class Arc {
    public Point start;
    public Point end;
    public Integer firstNodeNr;
    public Integer secondNodeNr;
    public Integer valoare=-1;

    public Arc(){
        this.start = new Point();
        this.end = new Point();
    }
    public Arc(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    public Arc(Arc arc) {
        this.start=new Point();
        this.start.x = arc.start.x;
        this.start.y = arc.start.y;
        this.end=new Point();
        this.end.x = arc.end.x;
        this.end.y = arc.end.y;
        this.firstNodeNr=arc.firstNodeNr;
        this.secondNodeNr=arc.secondNodeNr;
        this.valoare=arc.valoare;
    }

    public void drawArc(Graphics g) {
        if (this.start != null) {
            g.setColor(Color.RED);
            g.drawLine(this.start.x, this.start.y, this.end.x, this.end.y);
            g.setColor(Color.BLUE);
            g.drawString(Integer.toString(this.valoare),(this.start.x+this.end.x)/2,(this.start.y+this.end.y)/2);
        }

    }
}
