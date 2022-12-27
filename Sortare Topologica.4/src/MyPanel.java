import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JPanel;
import java.io.*;
import java.util.List;

public class MyPanel extends JPanel {

    private int nodeNr = 0;
    private int node_diam = 20;
    private Vector<Node> listaNoduri;
    private Vector<Arc> listaArce;
    Point pointStart = null;
    Point pointEnd = null;
    boolean isDragging = false;
    public Vector<Point> nodes = new Vector<Point>();
    public ArrayList<ArrayList<Integer>> adjListArray;
    public int[][] adjMatrix;

    void writeMatrix() {

        try {
            File adjacencyMatrix = new File("C:/Users/madah/Java/Tema4/matrice_adiacenta.txt");

            if (adjacencyMatrix.createNewFile()) {
                System.out.println("File created: " + adjacencyMatrix.getName());
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter writeMatrix = new FileWriter("matrice_adiacenta.txt");
            writeMatrix.write("Number of nodes in graph: ");
            writeMatrix.write(String.valueOf(listaNoduri.size()));
            writeMatrix.write("\n\n");
            writeMatrix.write("Adjacency matrix:");
            writeMatrix.write("\n\n");

            adjMatrix = new int[nodeNr + 1][nodeNr +1];

                for (int i = 1; i <= nodeNr; i++) {
                    for (int j = 1; j <= nodeNr; j++) {
                        if (nodes.contains(new Point(i, j))) {
                            writeMatrix.write("1 ");
                            adjMatrix[i][j] = 1;
                        } else {
                            writeMatrix.write("0 ");
                            adjMatrix[i][j] = 0;
                        }
                    }
                    writeMatrix.write('\n');
                }
            writeMatrix.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void makeAdjList()
    {
        int l = adjMatrix[0].length;

        adjListArray = new ArrayList<ArrayList<Integer>>(l);

        for (int i = 0; i < l; i++) {
            adjListArray.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < adjMatrix[0].length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                if (adjMatrix[i][j] == 1) {
                    adjListArray.get(i).add(j);
                }
            }
        }
    }

    public boolean isOverlaping(int coordX, int coordY) {

        for (int i = 0; i < listaNoduri.size(); i++) {
            if (Math.sqrt((coordX - listaNoduri.elementAt(i).getCoordX()) * (coordX - listaNoduri.elementAt(i).getCoordX()) + (coordY - listaNoduri.elementAt(i).getCoordY()) * (coordY - listaNoduri.elementAt(i).getCoordY())) < 40) {
                return true;
            }
        }
        return false;
    }

    public boolean LineBetweenTwoNodes(Point pointStart, Point pointEnd) {

        Point p = new Point();

        boolean ok1 = false;
        boolean ok2 = false;

        for (int i = 0; i < listaNoduri.size(); i++) {
            if (pointStart.getX() >= listaNoduri.elementAt(i).getCoordX()
                    && pointStart.getX() <= listaNoduri.elementAt(i).getCoordX() + node_diam

                    && pointStart.getY() >= listaNoduri.elementAt(i).getCoordY()
                    && pointStart.getY() <= listaNoduri.elementAt(i).getCoordY() + node_diam) {

                p.x = listaNoduri.get(i).getNumber();
                ok1 = true;
            }
        }

        for (int j = 0; j < listaNoduri.size(); j++){
            if (pointEnd.getX() >= listaNoduri.elementAt(j).getCoordX()
                    && pointEnd.getX() <= listaNoduri.elementAt(j).getCoordX() + node_diam

                    && pointEnd.getY() >= listaNoduri.elementAt(j).getCoordY()
                    && pointEnd.getY() <= listaNoduri.elementAt(j).getCoordY() + node_diam) {

                p.y = listaNoduri.get(j).getNumber();
                ok2 = true;
            }
        }

        if (ok1 && ok2 ) {
                nodes.addElement(p);
                return true;
        }
        return false;
    }

    public MyPanel() {

        listaNoduri = new Vector<Node>();
        listaArce = new Vector<Arc>();

            addMouseListener(new MouseAdapter() {
                //evenimentul care se produce la apasarea mousse-ului
                public void mousePressed(MouseEvent e) {
                    pointStart = e.getPoint();
                }

                //evenimentul care se produce la eliberarea mousse-ului
                public void mouseReleased(MouseEvent e) {
                    if (!isDragging) {
                        if (!isOverlaping(e.getX(), e.getY())) {
                            addNode(e.getX(), e.getY());
                        }
                    } else {
                        if (LineBetweenTwoNodes(pointStart, pointEnd)) {
                            Arc arc = new Arc(pointStart, pointEnd);
                            listaArce.add(arc);
                        }
                    }
                    pointStart = null;
                    isDragging = false;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                //evenimentul care se produce la drag&drop pe mousse
                public void mouseDragged(MouseEvent e) {
                    pointEnd = e.getPoint();
                    isDragging = true;
                    repaint();
                }
            });

        topologicalSort();
    }

    private void addNode(int x, int y) {
        Node node = new Node(x, y, nodeNr + 1);
        listaNoduri.add(node);
        nodeNr++;
        repaint();
    }

    //se executa atunci cand apelam repaint()
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//apelez metoda paintComponent din clasa de baza

        //deseneaza arcele existente in lista
        for (Arc a : listaArce) {
            a.drawDirectedArc(g);
        }

        //deseneaza arcul curent; cel care e in curs de desenare
        if (pointStart != null) {
                g.setColor(Color.BLACK);
                g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);

                int dx = pointEnd.x - pointStart.x;
                int dy = pointEnd.y - pointStart.y;
                double D = Math.sqrt(dx*dx + dy*dy);
                double xm = D - 15;
                double xn = xm;
                double ym = 15;
                double yn = - 15, x;
                double sin = dy / D;
                double cos = dx / D;

                x = xm*cos - ym*sin +pointStart.x;
                ym = xm*sin + ym*cos + pointStart.y;
                xm = x;

                x = xn*cos - yn*sin + pointStart.x;
                yn = xn*sin + yn*cos + pointStart.y;
                xn = x;

                int[] xpoints = {pointEnd.x, (int) xm, (int) xn};
                int[] ypoints = {pointEnd.y, (int) ym, (int) yn};

                Polygon arrow;
                arrow = new Polygon(xpoints, ypoints, 3);
                g.fillPolygon(arrow);
            repaint();
        }

        //deseneaza lista de noduri
        for (int i = 0; i < listaNoduri.size(); i++) {
            listaNoduri.elementAt(i).drawNode(g, node_diam);
        }

        writeMatrix();
        makeAdjList();
    }

    /* Pentru a evita sortarea nodurilor in functie de timpul de finalizare, se poate folosi o
    stiva ce retine aceste noduri in ordinea terminarii parcurgerii */
    void topologicalSortUtil(int v, boolean[] visited, Stack stack)
    {
        visited[v] = true; //vizitam un nod
        Integer integer;

        Iterator<Integer> it = adjListArray.get(v).iterator(); //verificam lista de adiacenta a nodului pe care il vizitam

        while (it.hasNext())
        {
            integer = it.next();
            if (!visited[integer]) //verific recursiv nodurile nevizitate din lista de adiacenta
                topologicalSortUtil(integer, visited, stack);
        }

     //dupa ce am finalizat de analizat lista de adiacenta a nodului il introduc pe stiva
        integer = Integer.valueOf(v);
        stack.push(integer);
    }

   void topologicalSort()
    {
        Stack stack = new Stack();
        boolean[] nodeVisited = new boolean[nodeNr + 1]; //initial, marcam toate nodurile ca fiind nevizitate

        for (int i = 1; i <= nodeNr; i++) {
            if (!nodeVisited[i]) {
                topologicalSortUtil(i, nodeVisited, stack); // Parcurg toate nodurile nevizitate din graf si apelez recursiv metoda
                                                        // care analizeaza nodurile si le pune pe stiva
            }

            else {
                break;
            }
        }
    /* La afișarea ulterioară a nodurilor din stivă, primul nod scos din stivă va fi ultimul introdus (cu timpul de
    finalizare cel mai mare), iar ultimul nod scos din stivă va avea timpul de finalizare cel mai mic (fiind primul introdus). */
       while (!stack.empty()) {
           System.out.print(stack.pop() + " ");
        }
    }

    //Graful este aciclic daca si numai daca orice parcurgere totala DF a grafului nu produce arce de revenire.
    boolean isCyclicUtil(int i, boolean[] visited, boolean[] inStack) {

        if (inStack[i])
            return true;

        if (visited[i])
            return false;

        visited[i] = true;

        inStack[i] = true;
        List<Integer> children = adjListArray.get(i);

        for (Integer c: children)
            if (isCyclicUtil(c, visited, inStack))
                return true;

        inStack[i] = false;
        return false;
    }

    public boolean isCyclic()
    {
        boolean[] visited = new boolean[nodeNr + 1];
        boolean[] inStack = new boolean[nodeNr + 1];

        for (int i = 1; i < nodeNr; i++)
            if (isCyclicUtil(i, visited, inStack))
                return true;
        return false;
    }
}