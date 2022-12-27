import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class MyPanel extends JPanel {
    private int nodeNr = 0;
    private int nodeDiam = 30;
    public Vector<Node> listaNoduri;
    public Vector<Arc> listaArce;
    Point pointStart = null;
    Point pointEnd = null;
    private boolean isDragging = false;
    private boolean grafOrientat = false;
    private boolean repozitionareGraf =false;
    private int indexNodMutat;

    void deleteGraf(Vector<Node> listaNoduri, Vector<Arc> listaArce) {
        listaArce.clear();
        listaNoduri.clear();
        nodeNr = 0;
        repaint();
    }

    boolean canAddNode(int x, int y) {
        for (int i = 0; i < listaNoduri.size(); i++)
            if (Math.sqrt((Math.pow((listaNoduri.elementAt(i).getCoordX() - x), 2)) +
                    (Math.pow((listaNoduri.elementAt(i).getCoordY() - y), 2))) < 40)
                return false;
        return true;
    }

    // functie folosita pt a verifica daca coordonatele mouse-ului se afla pe surpafata unui nod
    boolean nodeSurface(int x, int y, Point p) {
        return (((x < p.getX()) && (p.getX() < x + nodeDiam)) && ((y < p.getY()) && (p.getY() < y + nodeDiam)));
    }

    public MyPanel() {
        listaNoduri = new Vector<Node>();
        listaArce = new Vector<Arc>();
        setBorder(BorderFactory.createLineBorder(Color.black));

        MyPanel f = this;
        JToggleButton b = new JToggleButton("Graf orientat");
        b.setBounds(50, 100, 95, 30);
        f.add(b);
        b.setVisible(true);


        JToggleButton b1 = new JToggleButton("Repozitionare graf ");
        b1.setBounds(20, 100, 95, 30);
        f.add(b1);
        b1.setVisible(true);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
                boolean selected = abstractButton.getModel().isSelected();
                if (selected) {
                    grafOrientat = true;
                } else {
                    grafOrientat = false;
                }
            }
        };
        b.addActionListener(actionListener);

        ActionListener actionListener1 = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton abstractButton1 = (AbstractButton) actionEvent.getSource();
                boolean selected1 = abstractButton1.getModel().isSelected();
                if (selected1) {
                    repozitionareGraf = true;
                } else {
                    repozitionareGraf = false;
                }
            }
        };
        b1.addActionListener(actionListener1);

        addMouseListener(new MouseAdapter() {
            //evenimentul care se produce la apasarea mousse-ului
            public void mousePressed(MouseEvent e) {
                pointStart = e.getPoint();
                if (repozitionareGraf) {
                    for (int i = 0; i < listaNoduri.size(); i++) {
                        if
                        (nodeSurface(listaNoduri.elementAt(i).getCoordX(), listaNoduri.elementAt(i).getCoordY(), pointStart)) {
                            indexNodMutat = i;
                        }
                    }
                }
            }

            //evenimentul care se produce la eliberarea mousse-ului
            public void mouseReleased(MouseEvent e) {
                if (!repozitionareGraf) {
                    if (!isDragging) {
                        //adaugam primul nod in lista
                        if (listaNoduri.size() == 0)
                            addNode(e.getX(), e.getY());
                        else
                            //verificam distanta intre punctul curent si fiecare nod din lista
                            if ((canAddNode(e.getX(), e.getY())))
                                addNode(e.getX(), e.getY());

                    } else {

                        //verificam daca pointStart este pe suprafata unui nod
                        for (int i = 0; i < listaNoduri.size(); i++) {
                            if (nodeSurface(listaNoduri.elementAt(i).getCoordX(), listaNoduri.elementAt(i).getCoordY(), pointStart)) {

                                //verificam daca pointEnd este pe suprafata unui nod
                                for (int j = 0; j < listaNoduri.size(); j++) {
                                    if (nodeSurface(listaNoduri.elementAt(j).getCoordX(), listaNoduri.elementAt(j).getCoordY(), pointEnd)) {

                                        //daca ambele conditii sunt indeplinite si nodurile sunt diferite, se adauga arcul in listaArce
                                        if (i != j) {
                                            Arc arc = new Arc(listaNoduri.elementAt(i),listaNoduri.elementAt(j));
                                            listaArce.add(arc);

                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    pointStart = null;
                    isDragging = false;

                    //se redeseneaza graful
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            //evenimentul care se produce la drag&drop pe mousse
            public void mouseDragged(MouseEvent e) {
                pointEnd = e.getPoint();
                isDragging = true;
                if (!repozitionareGraf) {
                    for (int i = 0; i < listaNoduri.size(); i++) {
                        if (nodeSurface(listaNoduri.elementAt(i).getCoordX(), listaNoduri.elementAt(i).getCoordY(), pointStart)) {
                            repaint();
                        }
                    }
                } else {
                    if (listaNoduri.elementAt(indexNodMutat) != null) {

                        //retinem coord vechi ale nodului
                        int xVechi = listaNoduri.elementAt(indexNodMutat).getCoordX();
                        int yVechi = listaNoduri.elementAt(indexNodMutat).getCoordY();

                        if(canAddNode(e.getX(),e.getY())) {
                            //actualizam coordonatele nodului cu coord pointEnd
                            listaNoduri.elementAt(indexNodMutat).setCoordX(e.getX());
                            listaNoduri.elementAt(indexNodMutat).setCoordY(e.getY());
                        }
                        repaint();
                    }
                }
            }
        });

    }

    //metoda care se apeleaza la eliberarea mouse-ului
    public void addNode(int x, int y) {
        Node node = new Node(x, y, nodeNr);
        listaNoduri.add(node);
        nodeNr++;
        repaint();
    }

    //se executa atunci cand apelam repaint()
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//apelez metoda paintComponent din clasa de baza

        //daca este graf orientat, se trag arce cu sageata in capat
        if (grafOrientat) {
            for (Arc a : listaArce) {
                a.drawArrow(g);
            }
            //deseneaza arcul curent; cel care e in curs de desenare
            if (pointStart != null && !repozitionareGraf) {
                g.setColor(Color.black);
                g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
            }
        }

        //daca nu este graf orientat, se trag arce fara sageata
        else {
            for (Arc a : listaArce) {
                a.drawArc(g);
            }
            //deseneaza arcul curent; cel care e in curs de desenare
            if (pointStart != null && !repozitionareGraf) {
                g.setColor(Color.black);
                g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
            }
        }

        //deseneaza lista de noduri pentru graf orientat/neorientat
        for (int i = 0; i < listaNoduri.size(); i++) {
            listaNoduri.elementAt(i).drawNode(g, nodeDiam);
        }
    }
}