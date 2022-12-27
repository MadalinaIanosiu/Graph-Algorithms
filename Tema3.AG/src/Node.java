import java.awt.*;
import java.util.HashMap;

public class Node {

    // fiecare nod are propria lista de adiacenta, astfel cand pornim de la un nod in algoritmul BFS vom accesa lista acelui nod
    public HashMap<Integer, Node> adjacentList;
    public Node predecessor;
    public Integer length;
    private int coordX;
    private int coordY;
    private int passable;
    private boolean exit;
    public int nodeNumber;

    public Node(int coordX, int coordY, int passable, int nodeNumber){
        this.coordX = coordX;
        this.coordY = coordY;
        this.passable = passable;
        this.nodeNumber = nodeNumber;
        adjacentList = new HashMap<>();
        exit = false;
        predecessor = null;
        length = 0;
    }
    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setExit(boolean exit){
        this.exit = exit;
    }

    public boolean getExit(){
        return exit;
    }

    public void setPass(int pass) {
        this.passable = pass;
    }

    public int getPass() {
        return passable;
    }
    // 0 -> se poate trece
    // 1 -> nu se poate trece
    // 2 -> nodul de start

    public void drawNode(Graphics g, int node_diam){
        if(passable == 0) {
            // daca avem valoarea 0 atunci putem trece prin nod
            g.setColor(Color.magenta);
            g.fillRect(coordX - node_diam / 2, coordY - node_diam / 2, node_diam, node_diam);
        }
        else if(passable == 1){
            g.setColor(Color.GRAY);
            g.fillRect(coordX - node_diam / 2, coordY - node_diam / 2, node_diam, node_diam);
        }
        //nodul de pornire
        else if(passable == 2){
            g.setColor(Color.orange);
            g.fillRect(coordX - node_diam / 2, coordY - node_diam / 2, node_diam, node_diam);
        }
    }
}

