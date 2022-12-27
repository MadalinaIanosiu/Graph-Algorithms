import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Panel extends JPanel {

    private File file;
    private Point start = new Point(150,150); // punctul de unde incepe desenarea nodurilor
    private int lines, columns;
    private ArrayList<ArrayList<Integer>> Matrix = new ArrayList<>();
    private Vector<Node> nodeVect;
    private Vector<Arc> arcVect;
    private final int nodeDiam = 50;
    private int start_index; // nodul de la care incepem algoritmul BFS
    private int nodeNumber = 0;
    private Vector<Node> exitNodes; // vectorul cu toate iesirile valide din nodul de la care pornim
    Map<Node,Node> P; // vectorul de predecesori
    private HashMap<Integer,Node> N; // noduri prin care se poate trece
    public Panel(String s){
        file = new File(s);
        N = new HashMap<>();
        nodeVect = new Vector<>();
        arcVect = new Vector<>();
        exitNodes = new Vector<>();
        P = new HashMap<>();
    }
    public void readMatrix() throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        lines =  scanner.nextInt();

        columns = scanner.nextInt();

        for(int i =0; i < lines; i++){
            Matrix.add(new ArrayList<>());
            for(int j = 0; j < columns; j++){
                Matrix.get(i).add(scanner.nextInt());
            }
        }
    }
    public void initGraph(){
        int startX = start.x;
        for(int i =0; i < lines; i++){
            for(int j = 0; j < columns; j++){
                addNode(start.x, start.y, i, j);
                start.x += 100;
                if(Matrix.get(i).get(j) == 0)
                    N.put(nodeNumber, nodeVect.lastElement()); // daca se poate trece prin nodul respectiv, il adaugam si in N
                nodeNumber++;
            }
            start.x = startX;
            start.y += 100;
        }


        for(Node n : N.values()){

            // daca se poate trece prin urmatorul nod, formam arc, astfel introducem nodurile in listele de adiacenta ale fiecaruia
            if(N.containsKey(n.nodeNumber+1) && (n.nodeNumber+1) % columns != 0){
                n.adjacentList.put(n.nodeNumber + 1, N.get(n.nodeNumber + 1)); // accesam lista de adiacenta a nodului curent din for
                N.get(n.nodeNumber+1).adjacentList.put(n.nodeNumber, n); // accesam nodul din vectorul de noduri fiindca stim ca e valid
            }

            // daca se poate trece prin nodul de dedesubt, formam arc
            if(N.containsKey(n.nodeNumber+columns)) {
                n.adjacentList.put(n.nodeNumber + columns, N.get(n.nodeNumber + columns));
                N.get(n.nodeNumber+columns).adjacentList.put(n.nodeNumber, n);
            }
        }

        repaint();
    }

    public void setStartNode(){ //aleg nodul de start

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {

                for (Node n : N.values()) {

                    if(n.getPass() == 2)
                        n.setPass(0);

                    if (euclideanDistance(me.getX(), n.getCoordX(), me.getY(), n.getCoordY()) <= nodeDiam/2
                    && n.getPass() == 0) { //selecteaza nodul pe care am apasat cu mouse ul
                        n.setPass(2);
                        start_index = n.nodeNumber;
                    }
                }
               BFS(start_index);
            }
        });
    }

    private void BFS(int start_index){
        Vector<Node> U = new Vector<>(); // U - multimea de noduri nevizitate
        Queue<Node> V = new LinkedList<>(); // V (noduri vizitate) va functiona ca si o coada
        P.clear();
        exitNodes.clear(); // stergem iesirile pentru nodul de start precedent
        arcVect.clear(); // stergem arcele precedente inainte sa cream noile drumuri

        Node x;
        Node y = null;

        for(int i : N.keySet()){ // iteram prin cheile existente in N(noduri prin care se poate trece)
            if(i != start_index){
                U.add(N.get(i)); //daca nu e nodul de pornire, il adaugam in U
            }
            else
                V.add(N.get(i)); // daca e nodul de la care pornim, il adaugam in V
        }

        // verificam daca nodul de pornire este o iesire, si daca este il afisam
        if(N.get(start_index).getExit() == true) {
            exitNodes.add(N.get(start_index));
            minPaths(exitNodes);
            return;
        }

        while(!V.isEmpty()) {
            x = V.poll(); // scoatem primul nod din coada

            // accesam lista nodului din care pornim si verificam toate nodurile cu care formeaza arce
            for (int i : x.adjacentList.keySet()) {
                //daca arcul din lista de adiacenta se formeaza cu predecesorul, nu vom schimba valoarea lui y
                if(x.adjacentList.get(i) != P.get(y)) {
                    y = x.adjacentList.get(i);
                }

                if (U.contains(y)) {
                    U.remove(y);
                    V.add(y);
                    P.put(y, x);
                    y.length = x.length+1;
                    if(y.getExit() == true && !exitNodes.contains(y))
                        exitNodes.add(y);
                }
            }

           minPaths(exitNodes);
        }
    }

    // cautam toate drumurile de lungime minima in vectorul de iesiri
    private void minPaths(Vector<Node> exitNodes){
        int min = 1000;
        for(Node n : exitNodes){
            if(n.length < min)
                min = n.length;
        }

        for(Node n :exitNodes){
           if(n.length == min)
               procedurePath(P, n); //deseneaza drumul la final
        }
    }
    private void procedurePath(Map<Node,Node> P, Node y){

        // iau coordonatele intre nod si predecesor pentru formarea arcelor
        while(P.get(y) != null){
            Point pointStart = new Point(y.getCoordX(), y.getCoordY());
            Point pointEnd = new Point(P.get(y).getCoordX(), P.get(y).getCoordY());

            Arc arc = new Arc(pointStart, pointEnd);
            arcVect.add(arc);

            y = P.get(y);
        }
        repaint();
    }
    private double euclideanDistance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
    private void addNode(int x, int y, int i, int j) {
        Node node = new Node(x, y, Matrix.get(i).get(j), nodeNumber);
        if(i == 0 || i == lines-1 || j == 0 || j == columns-1) { // daca nodul se afla pe margine, modificam valoarea booleanei "exit"
            node.setExit(true);
        }
        nodeVect.add(node);
        repaint();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        for(int i = 0; i < nodeVect.size(); i++){
            nodeVect.elementAt(i).drawNode(g, nodeDiam);
        }

        for (Arc a : arcVect) {
            a.drawArc(g);
        }
    }
}
