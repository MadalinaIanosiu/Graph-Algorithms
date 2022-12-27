import java.awt.*;
import java.util.Random;

public class Node {
    private int coordX;
    private int coordY;
    private int number;
    boolean isVisited = false;
    public int id=-1; //numarul pe care il poarta nodul
    public int low=-1; //retinem id-ul celui mai mic nod precedent in care putem ajunge din nodul curent
    public boolean isOnStack=false;

    public Node(int coordX, int coordY, int number) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.number = number;
    }

    public int getCoordX() {
        return this.coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return this.coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void drawNode(Graphics g, int node_diam, Color color) {
        g.setColor(color);
        g.fillOval(this.coordX, this.coordY, node_diam, node_diam);
        g.setColor(Color.white);
        g.drawOval(this.coordX, this.coordY, node_diam, node_diam);
            g.setFont(new Font("TimesRoman", 1, 15));
            if (this.number < 10) {
                g.drawString(Integer.valueOf(this.number).toString(), this.coordX + 13, this.coordY + 20);
            } else {
                g.drawString(Integer.valueOf(this.number).toString(), this.coordX + 8, this.coordY + 20);
            }
    }

    double getDistance(Point point) {
        return Math.sqrt(Math.pow((coordX - point.x), 2) + Math.pow((coordY - point.y), 2));
    }
}
