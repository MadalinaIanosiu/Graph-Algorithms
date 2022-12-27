import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.PriorityQueue;
import java.util.Vector;
import javax.swing.*;

public class MyPanel extends JPanel {
    private int nodeNr = 1;
    private int node_diam = 30;
    private Vector<Node> listaNoduri = new Vector<>();
    private Vector<Arc> listaArce = new Vector<>();
    public Vector<Vector<Integer>> matriceDeAdiacenta = new Vector<>();
    Point pointStart = null;
    Point pointEnd = null;
    boolean isDragging = false;
    JButton buton;
    JTextField valoareaArcelor;
    JButton butonArce;
    PriorityQueue<SmallArc> pq = new PriorityQueue<>(10, new ArcComparator());
    private Vector<Arc> listaArcePrim = new Vector<>();
    private Vector<Arc> listaArceKruskal = new Vector<>();

    void prim() {
        Vector<Integer> esteVizitat = new Vector<>();
        for (int i = 0; i < listaNoduri.size(); i++) {
            esteVizitat.add(0); //initial setam toate nodurile drept "nevizitate"
        }
        int nodCurent = -1;
        for (int i = 0; i < esteVizitat.size(); i++) {
            if (esteVizitat.elementAt(i) == 0) {
                nodCurent = i;
                esteVizitat.setElementAt(1, nodCurent); //vizitam nodul curent
                break;
            }
        }
        //adaug toate arcele nodului curent in pq
        for (int i = 0; i < matriceDeAdiacenta.elementAt(nodCurent).size(); i++) {
            if (matriceDeAdiacenta.elementAt(nodCurent).elementAt(i) != -1 && esteVizitat.elementAt(i) == 0) {
                //System.out.println("Added: " + nodCurent + " " + i + " " + matriceDeAdiacenta.elementAt(nodCurent).elementAt(i));
                pq.add(new SmallArc(nodCurent, i, matriceDeAdiacenta.elementAt(nodCurent).elementAt(i)));
            }
        }
        // ne deplasam pe arcul cu cel mai mic cost
        nodCurent = pq.peek().secondNodeNr;
        for (int i = 0; i < listaArce.size(); i++) { //gasesc echivalentul arcului curent al arcului compactat, apoi adaug in lista arce
            if (listaArce.elementAt(i).firstNodeNr == pq.peek().firstNodeNr && listaArce.elementAt(i).secondNodeNr == nodCurent ||
                    listaArce.elementAt(i).secondNodeNr == pq.peek().firstNodeNr && listaArce.elementAt(i).secondNodeNr == nodCurent) {
                Arc auxPrim = new Arc(listaArce.elementAt(i));
                auxPrim.start.x += Graf.width / 2;
                auxPrim.end.x += Graf.width / 2;
                auxPrim.valoare = listaArce.elementAt(i).valoare;
                listaArcePrim.add(auxPrim);
            }
        }
        while (!pq.isEmpty()) {
            if (esteVizitat.elementAt(pq.peek().firstNodeNr) == 0) { //verif daca primul nod al celui mai mic arc este nevizitat,apoi il vizitam
                nodCurent = pq.peek().firstNodeNr;
                esteVizitat.setElementAt(1, nodCurent);
                for (int i = 0; i < listaArce.size(); i++) {
                    if (listaArce.elementAt(i).firstNodeNr == nodCurent && listaArce.elementAt(i).secondNodeNr == pq.peek().secondNodeNr ||
                            listaArce.elementAt(i).secondNodeNr == nodCurent && listaArce.elementAt(i).secondNodeNr == pq.peek().firstNodeNr) {
                        Arc auxPrim = new Arc(listaArce.elementAt(i));
                        auxPrim.start.x += Graf.width / 2;
                        auxPrim.end.x += Graf.width / 2;
                        auxPrim.valoare = listaArce.elementAt(i).valoare;
                        listaArcePrim.add(auxPrim);
                    }
                }
                pq.remove(); //stergem toate arcele din pq pentru ca o sa selectam alt nod curent

                for (int i = 0; i < matriceDeAdiacenta.elementAt(nodCurent).size(); i++) {
                    //parcurg vecinul i al nodului curent este nevzitat si exista arc de la nodul curent la vecinul i, il adaug in pq
                    if (esteVizitat.elementAt(i) == 0 && matriceDeAdiacenta.elementAt(nodCurent).elementAt(i) != -1) {
                        pq.add(new SmallArc(nodCurent, i, matriceDeAdiacenta.elementAt(nodCurent).elementAt(i)));
                    }
                }
            } else if (esteVizitat.elementAt(pq.peek().secondNodeNr) == 0) { //verific daca al doilea nod al celui mai mic arc este nevizitat
                nodCurent = pq.peek().secondNodeNr;
                esteVizitat.setElementAt(1, nodCurent);
                for (int i = 0; i < listaArce.size(); i++) {
                    if (listaArce.elementAt(i).firstNodeNr == nodCurent && listaArce.elementAt(i).secondNodeNr == pq.peek().firstNodeNr ||
                            listaArce.elementAt(i).secondNodeNr == nodCurent && listaArce.elementAt(i).firstNodeNr == pq.peek().firstNodeNr) {
                        Arc auxPrim = new Arc(listaArce.elementAt(i));
                        auxPrim.start.x += Graf.width / 2;
                        auxPrim.end.x += Graf.width / 2;
                        auxPrim.valoare = listaArce.elementAt(i).valoare;
                        listaArcePrim.add(auxPrim);
                    }
                }
                pq.remove();

                for (int i = 0; i < matriceDeAdiacenta.elementAt(nodCurent).size(); i++) {
                    if (esteVizitat.elementAt(i) == 0 && matriceDeAdiacenta.elementAt(nodCurent).elementAt(i) != -1) {
                        pq.add(new SmallArc(nodCurent, i, matriceDeAdiacenta.elementAt(nodCurent).elementAt(i)));
                    }
                }
            } else {
                pq.remove();
            }
        }
    }

    void kruskal() {
        //initializez vectorul de noduri vizitate cu 0
        Vector<Integer> esteVizitat = new Vector<>();
        for (int i = 0; i < listaNoduri.size(); i++) {
            esteVizitat.add(0);
        }

        Integer batchId = 0;
        Vector<Integer> batch = new Vector<>(); //reprezinta o multime de noduri, la inceput toate nodurile fac parte din aceeasi multime
        for (int i = 0; i < listaNoduri.size(); i++) {
            batch.add(-1);
        }

        //creez si sortez crescator arcele intr-un vector auxiliar; ca sa nu modificam vec de arce initial
        Vector<Arc> arceSortate = new Vector<>();
        for (int i = 0; i < listaArce.size(); i++) {
            Arc aux = new Arc(listaArce.elementAt(i));
            arceSortate.add(aux);
        }
        arceSortate.sort(new RealArcComparator());

        //cat timp am noduri care nu sunt vizitate, continui sa adaug arce
        int i = 0; // indexul i alege urmatoarea muchie(cu i parcurg arcele sortate incepand cu cel mai mic)
        boolean stop = false;
        while (!stop && i < arceSortate.size() - 1) { //cat timp mai sunt arce pe care sa ma deplasez
            if (esteVizitat.elementAt(arceSortate.elementAt(i).firstNodeNr) == 0 && //verific daca ambele capete(noduri) ale arcului sunt nevizitate
                    esteVizitat.elementAt(arceSortate.elementAt(i).secondNodeNr) == 0) {
                esteVizitat.setElementAt(1, arceSortate.elementAt(i).firstNodeNr);
                esteVizitat.setElementAt(1, arceSortate.elementAt(i).secondNodeNr);
                batch.setElementAt(batchId, arceSortate.elementAt(i).firstNodeNr); // adaug id nodurilor in aceeasi multime
                batch.setElementAt(batchId, arceSortate.elementAt(i).secondNodeNr);
                batchId++; //cresc numarul de noduri ale multimii
                Arc aux = arceSortate.elementAt(i);
                aux.start.y += Graf.height / 2;
                aux.start.x+= Graf.width / 3;
                aux.end.y += Graf.height / 2;
                aux.end.x+= Graf.width / 3;
                listaArceKruskal.add(aux);
            }
            else if (esteVizitat.elementAt(arceSortate.elementAt(i).firstNodeNr) == 1 &&
                    esteVizitat.elementAt(arceSortate.elementAt(i).secondNodeNr) == 0) //verificam daca primul nod e vizitat si al doilea nevizitat
            { esteVizitat.setElementAt(1, arceSortate.elementAt(i).secondNodeNr); //vizitez al doilea nod care era nevizitat
                batch.setElementAt(batch.elementAt(arceSortate.elementAt(i).firstNodeNr), arceSortate.elementAt(i).secondNodeNr);
                Arc aux = arceSortate.elementAt(i);
                aux.start.y += Graf.height / 2;
                aux.start.x+= Graf.width / 3;
                aux.end.y += Graf.height / 2;
                aux.end.x+= Graf.width / 3;
                listaArceKruskal.add(aux); //adaug al doilea nod in multimea din care face parte primul
            }
            else if (esteVizitat.elementAt(arceSortate.elementAt(i).firstNodeNr) == 0 && esteVizitat.elementAt(arceSortate.elementAt(i).secondNodeNr) == 1) {
                esteVizitat.setElementAt(1, arceSortate.elementAt(i).firstNodeNr);
                batch.setElementAt(batch.elementAt(arceSortate.elementAt(i).secondNodeNr), arceSortate.elementAt(i).firstNodeNr);
                Arc aux = arceSortate.elementAt(i);
                aux.start.y += Graf.height / 2;
                aux.start.x+= Graf.width / 3;
                aux.end.y += Graf.height / 2;
                aux.end.x+= Graf.width / 3;
                listaArceKruskal.add(aux);
            }
            //verificam daca nodurile fac parte din multimi diferite
            else if (batch.elementAt(arceSortate.elementAt(i).firstNodeNr) != batch.elementAt(arceSortate.elementAt(i).secondNodeNr)) {
                int batch1 = batch.elementAt(arceSortate.elementAt(i).firstNodeNr);
                int batch2 = batch.elementAt(arceSortate.elementAt(i).secondNodeNr);
                for (int j = 0; j < batch.size(); j++) { //adugam in multime nodul care contine arcul de cost minim
                    if (batch.elementAt(j) == batch1 || batch.elementAt(j) == batch2) {
                        batch.setElementAt(Math.min(batch1, batch2), j);
                    }
                }
                Arc aux = arceSortate.elementAt(i);
                aux.start.y += Graf.height / 2;
                aux.start.x+= Graf.width / 3;
                aux.end.y += Graf.height / 2;
                aux.end.x+= Graf.width / 3;
                listaArceKruskal.add(aux);
            }

            Integer check = batch.elementAt(0);
            boolean diferite = false;

            for (int j = 1; j < batch.size(); j++) {
                if (batch.elementAt(j) != check) {
                    diferite = true;
                    break;
                }
            }
            if (!diferite)
                stop = true;
            i++;
        }


    }

    void buttonX() { //deseneaza arcele arborilor de cost minim
        listaArcePrim.clear();
        prim();
        listaArceKruskal.clear();
        kruskal();

        repaint();
    }

    void functieButonArce() { //adaug costul unei muchii
        String aux = valoareaArcelor.getText();
        int firstNode = Integer.parseInt(aux.substring(0, aux.indexOf(" "))) - 1;
        int secondNode = Integer.parseInt(aux.substring(aux.indexOf(" ") + 1, aux.indexOf(" ", aux.indexOf(" ") + 1))) - 1;
        int arcValue = Integer.parseInt(aux.substring(aux.lastIndexOf(" ") + 1));
        for (int i = 0; i < listaArce.size(); i++) {
            if (listaArce.elementAt(i).firstNodeNr == firstNode &&
                    listaArce.elementAt(i).secondNodeNr == secondNode
                    || listaArce.elementAt(i).secondNodeNr == firstNode &&
                    listaArce.elementAt(i).firstNodeNr == secondNode) {
                listaArce.elementAt(i).valoare = arcValue;
                matriceDeAdiacenta.elementAt(firstNode).setElementAt(arcValue, secondNode);
                matriceDeAdiacenta.elementAt(secondNode).setElementAt(arcValue, firstNode);
                repaint();
                break;

            }
        }

    }

    public MyPanel() {
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        buton = new JButton("Arbori partiali minimi");
        buton.setBounds(75, 100, 100, 50);
        buton.addActionListener(e -> buttonX());
        add(buton);

        valoareaArcelor = new JTextField();
        valoareaArcelor.setPreferredSize(new Dimension(250, 40));
        add(valoareaArcelor);

        butonArce = new JButton("Adauga valoarea");
        butonArce.setBounds(75, 100, 100, 50);
        butonArce.addActionListener(e -> functieButonArce());
        add(butonArce);

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
                            for (Vector<Integer> integers : matriceDeAdiacenta) {
                                integers.add(-1);

                            }
                            Vector<Integer> row = new Vector<>();
                            for (int j = 0; j < matriceDeAdiacenta.get(0).size(); j++) {
                                row.add(-1);
                            }
                            matriceDeAdiacenta.add(row);
                            addNode(e.getX() - node_diam / 2, e.getY() - node_diam / 2);
                        }
                    } else {
                        Vector<Integer> row = new Vector<>();
                        row.add(-1);
                        matriceDeAdiacenta.add(row);
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
                        matriceDeAdiacenta.get(pointStartNodeNumber - 1).set(pointEndNodeNumber - 1, 1);
                        matriceDeAdiacenta.get(pointEndNodeNumber - 1).set(pointStartNodeNumber - 1, 1);
                        Arc aux = new Arc(new Point(listaNoduri.elementAt(pointStartNodeNumber - 1).getCoordX() + node_diam / 2, listaNoduri.elementAt(pointStartNodeNumber - 1).getCoordY() + node_diam / 2), new Point(listaNoduri.elementAt(pointEndNodeNumber - 1).getCoordX() + node_diam / 2, listaNoduri.elementAt(pointEndNodeNumber - 1).getCoordY() + node_diam / 2));
                        aux.firstNodeNr = pointStartNodeNumber - 1;
                        aux.secondNodeNr = pointEndNodeNumber - 1;
                        listaArce.add(aux);
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
    }

    private void addNode(int x, int y) {
        Node node = new Node(x, y, this.nodeNr);
        this.listaNoduri.add(node);
        ++this.nodeNr;
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Prim", Graf.width * 3 / 4, Graf.height / 2 - 50);
        g.drawString("Kruskal", 500 , 600);

        for (Arc a : this.listaArce) {
            a.drawArc(g);
        }
        for (Arc a : this.listaArcePrim) {
            a.drawArc(g);
        }
        for (Arc a : this.listaArceKruskal) {
            a.drawArc(g);
        }

        if (this.pointStart != null) {
            g.setColor(Color.RED);
            g.drawLine(this.pointStart.x, this.pointStart.y, this.pointEnd.x, this.pointEnd.y);
        }

        for (int i = 0; i < this.listaNoduri.size(); ++i) {
            this.listaNoduri.elementAt(i).drawNode(g, this.node_diam, 0, 0);
            this.listaNoduri.elementAt(i).drawNode(g, this.node_diam, Graf.width / 2, 0);
            this.listaNoduri.elementAt(i).drawNode(g, this.node_diam, Graf.width/3, Graf.height / 2);
        }
    }
}
