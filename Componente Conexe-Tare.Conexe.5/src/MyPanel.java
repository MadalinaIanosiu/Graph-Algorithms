import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;
import javax.swing.*;

public class MyPanel extends JPanel {
    private int nodeNr = 1;
    private final int node_diam = 30;
    private final Vector<Node> listaNoduri = new Vector<>();
    private final Vector<Arc> listaArce = new Vector<>();
    public Vector<Color> listaCuloriNoduri = new Vector<>();
    public Vector<Color> listaCuloriArce = new Vector<>();
    Point pointStart = null;
    Point pointEnd = null;
    public Vector<Vector<Integer>> listaDeAdiacenta = new Vector<>();
    public Queue<Integer> coada = new LinkedList<>();
    boolean isDragging = false;
    Random randColor = new Random();
    Stack<Integer> stiva = new Stack<>();
    int ids = 0;

    public Color generateRandColor() {
        float R = randColor.nextFloat();
        float G = randColor.nextFloat();
        float B = randColor.nextFloat();
        return new Color(R, G, B);
    }

    public Color batch;

    public void componenteConexe() {
        boolean stop = false;
        while (!stop) { //cat timp gasim noduri nevizitate
            stop = true;
            batch = generateRandColor();
            for (int i = 0; i < listaNoduri.size() && stop; i++) { //parcurgem lista de noduri, iar pe cele nevizitate
                if (!listaNoduri.elementAt(i).isVisited) {         //le adaugam in coada, apoi le marcam drept vizitate
                    stop = false;
                    coada.add(i);
                    listaCuloriNoduri.set(i, batch); //setam culoarea pentru nodul curent
                    listaNoduri.elementAt(i).isVisited = true;
                }
            }

            while (!coada.isEmpty()) {
                for (int i = 0; i < listaDeAdiacenta.elementAt(coada.element()).size(); i++) { //verificam toate nodurile din lista de adiacenta a primului elem din coada
                    if (!listaNoduri.elementAt(listaDeAdiacenta.elementAt(coada.element()).elementAt(i)).isVisited) {
                        //daca elementul de pe pozitia curenta din lista de adiacenta nu a fost vizitat, adaugam elem in coada
                        coada.add(listaDeAdiacenta.elementAt(coada.element()).elementAt(i));
                        listaCuloriNoduri.set(listaDeAdiacenta.elementAt(coada.element()).elementAt(i), batch);
                        listaNoduri.elementAt(listaDeAdiacenta.elementAt(coada.element()).elementAt(i)).isVisited = true;
                    }
                }
                coada.remove(); //eliminam primul elem din coada odata ce i-am terminat de analizat lista de adiacenta
            }
        }
    }

    void buttonCC() {
        componenteConexe();
        coloreazaArce();
        repaint();
        for (Node it : listaNoduri) {
            it.isVisited = false;
        }
    }

    void buttonCTC() {
        for (Node it : listaNoduri) {
            it.isVisited = false;
        }
        componenteTariConexe();
        coloreazaNoduriOrientate();
        coloreazaArce();
        repaint();
    }

    void coloreazaArce() {
        boolean stop;
        for (int i = 0; i < listaArce.size(); i++) {
            stop = false;
            for (int j = 0; j < listaNoduri.size() && !stop; j++) {
                if (listaArce.elementAt(i).start.x - node_diam / 2 == listaNoduri.elementAt(j).getCoordX() &&
                        listaArce.elementAt(i).start.y - node_diam / 2 == listaNoduri.elementAt(j).getCoordY()) {
                    listaCuloriArce.set(i, listaCuloriNoduri.elementAt(j));
                    stop = true;
                }
            }
        }
    }

    void coloreazaNoduriOrientate() {
        for (Node it : listaNoduri) {
            it.isVisited = false;
            it.isOnStack = false;
        }
        for (int i = 0; i < listaNoduri.size(); i++) {
            if (!listaNoduri.elementAt(i).isVisited) {
                listaNoduri.elementAt(i).isVisited = true;
                batch = generateRandColor();
                listaCuloriNoduri.set(i, batch);
                for (int j = 0; j < listaNoduri.size(); j++) {
                    if (!listaNoduri.elementAt(j).isVisited && listaNoduri.elementAt(j).low == listaNoduri.elementAt(i).low) {
                        listaNoduri.elementAt(j).isVisited = true;
                        listaCuloriNoduri.set(j, batch);
                    }
                }
            }
        }
    }

    //Tarjan’s Algorithm to find Strongly Connected Components -Algoritmul este centrat pe identificarea dacă un nod este sau nu rădăcină a
    //unei componente tare conexe.
    public void dfs(int at) {
        stiva.add(at);
        listaNoduri.elementAt(at).isOnStack = true;
        listaNoduri.elementAt(at).isVisited = true;
        listaNoduri.elementAt(at).id = listaNoduri.elementAt(at).low = ids++;

        if (listaDeAdiacenta.elementAt(at).size() == 0) { //daca lista de adiacenta a elem curent=0,scoatem elem din stiva
            listaNoduri.elementAt(stiva.peek()).isOnStack = false;
            stiva.pop();
        } else {
            for (Integer to : listaDeAdiacenta.elementAt(at)) { //vizitam toti vecinii nodului curent
                if (!listaNoduri.elementAt(to).isVisited) { //daca nodul la care suntem nu a fost  vizitat atunci il vizitam
                    dfs(to);
                }
                if (listaNoduri.elementAt(to).isOnStack) { //daca nodul vecin apare pe stiva, atunci updatam variabila low a nodului curent cu minimul
                    listaNoduri.elementAt(at).low = Math.min(listaNoduri.elementAt(at).low, listaNoduri.elementAt(to).low);
                }
                if (listaNoduri.elementAt(at).isOnStack) {
                    if (listaNoduri.elementAt(at).id == listaNoduri.elementAt(at).low) {//daca nodul curent are id = low-linked value=> nodul curent a inceput o SCC
                        if (stiva.peek() == at) {
                            listaNoduri.elementAt(stiva.peek()).isOnStack = false;
                            stiva.pop(); //daca nodul care alcatuieste componenta tare conexa este chiar vf stivei, il extragem
                        } else {
                            while (true) {
                                listaNoduri.elementAt(stiva.peek()).isOnStack = false;
                                listaNoduri.elementAt(stiva.peek()).low = listaNoduri.elementAt(at).id;//ne asiguram ca toate nodurile care alc comp conexa au acelasi id
                                stiva.pop(); //extragem toate nodurile care alcatuiesc componenta tare conexa
                                if (stiva.isEmpty()) {
                                    break;
                                }
                                if (stiva.peek() == at) {
                                    listaNoduri.elementAt(stiva.peek()).isOnStack = false;
                                    stiva.pop();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void componenteTariConexe() {
        ids = 0;
        stiva.clear();

        for (Node it : listaNoduri) {
            it.isVisited = false;
        }
        for (int i = 0; i < listaNoduri.size(); i++) {
            if (!listaNoduri.elementAt(i).isVisited)
                dfs(i);
        }
    }

    public MyPanel() {
        // borderul panel-ului
        if (Objects.equals(ButtonFrame.type, "Neorientat")) {
            JButton cc = new JButton("Componente conexe");
            cc.setBounds(75, 100, 100, 50);
            cc.addActionListener(e -> buttonCC());
            add(cc);
        }
        if (Objects.equals(ButtonFrame.type, "Orientat")) {
            JButton ctc = new JButton("Componente tari conexe");
            ctc.setBounds(705, 100, 100, 50);
            ctc.addActionListener(e -> buttonCTC());
            add(ctc);
        }

        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            // evenimentul care se produce la apasarea mouse-ului
            public void mousePressed(MouseEvent e) {
                pointStart = e.getPoint();
            }

            // evenimentul care se produce la eliberarea mouse-ului
            public void mouseReleased(MouseEvent e) {
                if (!isDragging) {// adaugare nod, fara sa se suprapuna
                    boolean isClose = false;
                    Point newNodeCenter = e.getPoint();
                    newNodeCenter.x = newNodeCenter.x - node_diam / 2;
                    newNodeCenter.y = newNodeCenter.y - node_diam / 2;
                    if (listaNoduri.size() > 0) {
                        for (Node it : listaNoduri) {
                            if (it.getDistance(newNodeCenter) <= node_diam) {
                                isClose = true;
                            }
                        }
                        if (!isClose) {
                            listaDeAdiacenta.add(new Vector<>());
                            addNode(e.getX() - node_diam / 2, e.getY() - node_diam / 2);
                        }
                    } else {
                        listaDeAdiacenta.add(new Vector<>());
                        addNode(e.getX() - node_diam / 2, e.getY() - node_diam / 2);
                    }
                } // adaugare arce care pleaca din noduri sau ajung in noduri
                else {
                    boolean pointStartIsOnNode = false;
                    int pointStartNodeNumber = 0;

                    boolean pointEndIsOnNode = false;
                    int pointEndNodeNumber = 0;

                    // pointStart
                    for (int it = 0; it < listaNoduri.size() && !pointStartIsOnNode; it++) {
                        if (listaNoduri.get(it).getDistance(pointStart) < node_diam) {
                            pointStartIsOnNode = true;
                            pointStartNodeNumber = listaNoduri.get(it).getNumber();

                        }
                    }

                    // pointEnd
                    for (int it = 0; it < listaNoduri.size() && !pointEndIsOnNode; it++) {
                        if (listaNoduri.get(it).getDistance(pointEnd) < node_diam) {
                            pointEndIsOnNode = true;
                            pointEndNodeNumber = listaNoduri.get(it).getNumber();
                        }
                    }

                    if (pointStartIsOnNode && pointEndIsOnNode && pointStartNodeNumber != pointEndNodeNumber) {
                        // aici updatam graful de adiacenta
                        if (Objects.equals(Graf.type, "Componente conexe")) {
                            boolean gasit = false;
                            for (Integer it : listaDeAdiacenta.elementAt(pointStartNodeNumber - 1)) {
                                if (it == pointEndNodeNumber - 1) {
                                    gasit = true;
                                    break;
                                }
                            }
                            if (!gasit) {
                                listaDeAdiacenta.elementAt(pointStartNodeNumber - 1).add(pointEndNodeNumber - 1);
                            }
                            for (Integer it : listaDeAdiacenta.elementAt(pointEndNodeNumber - 1)) {
                                if (it == pointStartNodeNumber - 1) {
                                    gasit = true;
                                    break;
                                }
                            }
                            if (!gasit) {
                                listaDeAdiacenta.elementAt(pointEndNodeNumber - 1).add(pointStartNodeNumber - 1);
                                Arc aux = new Arc(new Point(listaNoduri.elementAt(pointStartNodeNumber - 1).getCoordX() + node_diam / 2, listaNoduri.elementAt(pointStartNodeNumber - 1).getCoordY() + node_diam / 2), new Point(listaNoduri.elementAt(pointEndNodeNumber - 1).getCoordX() + node_diam / 2, listaNoduri.elementAt(pointEndNodeNumber - 1).getCoordY() + node_diam / 2));
                                listaArce.add(aux);
                                listaCuloriArce.add(Color.red);
                            }
                        }
                        if (Objects.equals(Graf.type, "Componente tari conexe")) {
                            boolean gasit = false;
                            for (Integer it : listaDeAdiacenta.elementAt(pointStartNodeNumber - 1)) {
                                if (it == pointEndNodeNumber - 1) {
                                    gasit = true;
                                    break;
                                }
                            }
                            if (!gasit) {
                                listaDeAdiacenta.elementAt(pointStartNodeNumber - 1).add(pointEndNodeNumber - 1);
                                Arc aux = new Arc(new Point(listaNoduri.elementAt(pointStartNodeNumber - 1).getCoordX() + node_diam / 2, listaNoduri.elementAt(pointStartNodeNumber - 1).getCoordY() + node_diam / 2), new Point(listaNoduri.elementAt(pointEndNodeNumber - 1).getCoordX() + node_diam / 2, listaNoduri.elementAt(pointEndNodeNumber - 1).getCoordY() + node_diam / 2));
                                listaArce.add(aux);
                                listaCuloriArce.add(Color.red);
                            }
                        }
                    }
                }
                pointStart = null;
                isDragging = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            // evenimentul care se produce la drag&drop pe mousse
            public void mouseDragged(MouseEvent e) {
                pointEnd = e.getPoint();
                isDragging = true;
                repaint();
            }
        });

        if (Objects.equals(ButtonFrame.type, "Orientat")) {
            componenteTariConexe();
            coloreazaNoduriOrientate();
            coloreazaArce();
        }
    }

    private void addNode(int x, int y) {
        Node node = new Node(x, y, this.nodeNr);
        listaCuloriNoduri.add(Color.red);
        this.listaNoduri.add(node);
        ++this.nodeNr;
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);// apelez metoda paintComponent din clasa de baza
        // deseneaza arcele existente in lista
        for (int i = 0; i < listaArce.size(); i++) {
            listaArce.elementAt(i).drawArc(g, listaCuloriArce.elementAt(i));
        }
        // deseneaza arcul curent; cel care e in curs de desenare
        if (pointStart != null) {
            g.setColor(Color.RED);
            g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
            if (Objects.equals(Graf.type, "Componente tari conexe")) {
                double angle = Math.atan2(pointEnd.y - pointStart.y, pointEnd.x - pointStart.x);
                int arrowHeight = 15;
                int halfArrowWidth = 7;
                Point aroBase = new Point(0, 0);
                aroBase.x = (int) (pointEnd.x - arrowHeight * Math.cos(angle));
                aroBase.y = (int) (pointEnd.y - arrowHeight * Math.sin(angle));
                Point varf1 = new Point(0, 0), varf2 = new Point(0, 0);
                varf1.x = (int) (aroBase.x - halfArrowWidth * Math.cos(angle - Math.PI / 2));
                varf1.y = (int) (aroBase.y - halfArrowWidth * Math.sin(angle - Math.PI / 2));
                varf2.x = (int) (aroBase.x + halfArrowWidth * Math.cos(angle - Math.PI / 2));
                varf2.y = (int) (aroBase.y + halfArrowWidth * Math.sin(angle - Math.PI / 2));
                int[] x = {pointEnd.x, varf1.x, varf2.x};
                int[] y = {pointEnd.y, varf1.y, varf2.y};
                int npoints = x.length;// or y.length
                g.drawPolygon(x, y, npoints);// draws polygon outline
                g.fillPolygon(x, y, npoints);// paints a polygon
            }
        }
        // deseneaza lista de noduri
        for (int i = 0; i < listaNoduri.size(); i++) {
            listaNoduri.elementAt(i).drawNode(g, node_diam, listaCuloriNoduri.elementAt(i));
        }
    }
}
