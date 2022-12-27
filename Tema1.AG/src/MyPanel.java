import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;


public class MyPanel extends JPanel {
    private int nodeNr = 0;
    private int nodeDiam = 30;
    private Vector<Node> listaNoduri;
    private Vector<Arc> listaArce;
    Point pointStart = null;
    Point pointEnd = null;
    boolean isDragging = false;
    boolean grafOrientat = false;
    private int[][] matriceAdiacenta = new int[200][200];
    boolean modMutareNoduri = false;
    int indexNodMutat;

    void initMatrice(int[][] matriceAdiacenta) {
        for (int index = 0; index < listaNoduri.size(); index++) {
            for (int index2 = 0; index2 < listaNoduri.size(); index2++) {
                matriceAdiacenta[index][index2] = 0;
            }
        }
    }

    void deleteGraf(Vector<Node> listaNoduri, Vector<Arc> listaArce) {
        listaArce.clear();
        listaNoduri.clear();
        initMatrice(matriceAdiacenta);
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

    boolean nodeSurface(int x, int y, Point p) {
        return (((x < p.getX()) && (p.getX() < x + nodeDiam)) && ((y < p.getY()) && (p.getY() < y + nodeDiam)));
    }

    void afisareMatriceAdiacenta(int[][] matriceAdiacenta) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("MatriceAdiacenta.txt"));
            out.println("In graf sunt " + nodeNr + " noduri");
            out.print("\n");
            out.println("Matricea de adiacenta: ");
            for (int index = 0; index < listaNoduri.size(); index++) {
                for (int index2 = 0; index2 < listaNoduri.size(); index2++) {
                    out.print(matriceAdiacenta[index][index2] + " ");
                }
                out.print("\n");
            }
            out.close();
        } catch (IOException e1) {
            System.out.println("Eroare la scrierea in fisierul MatriceAdiacenta.txt");
        }
    }

    public MyPanel() {
        listaNoduri = new Vector<Node>();
        listaArce = new Vector<Arc>();
        initMatrice(matriceAdiacenta);

        // borderul panel-ului
        setBorder(BorderFactory.createLineBorder(Color.black));

        MyPanel f = this;
        JToggleButton b = new JToggleButton("Graf orientat");
        b.setBounds(50, 100, 95, 30);
        f.add(b);
        b.setVisible(true);

        JToggleButton b1 = new JToggleButton("Repozitionare graf ");
        b.setBounds(20, 100, 95, 30);
        f.add(b1);
        b1.setVisible(true);

        //daca butonul b e apasat, se intra in modul de graf orientat
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
                boolean selected = abstractButton.getModel().isSelected();
                if (selected) {
                    grafOrientat = true;
                    deleteGraf(listaNoduri, listaArce);
                } else {
                    grafOrientat = false;
                    deleteGraf(listaNoduri, listaArce);
                }
            }
        };
        b.addActionListener(actionListener);

        //daca butonul b1 e apasat, se intra in modul de repozitionare graf
        ActionListener actionListener1 = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton abstractButton1 = (AbstractButton) actionEvent.getSource();
                boolean selected1 = abstractButton1.getModel().isSelected();
                if (selected1) {
                    modMutareNoduri = true;
                } else {
                    modMutareNoduri = false;
                }
            }
        };
        b1.addActionListener(actionListener1);

        addMouseListener(new MouseAdapter() {
            //evenimentul care se produce la apasarea mousse-ului
            public void mousePressed(MouseEvent e) {
                pointStart = e.getPoint();
                if (modMutareNoduri) {
                    for (int i = 0; i < listaNoduri.size(); i++) {
                        if (nodeSurface(listaNoduri.elementAt(i).getCoordX(), listaNoduri.elementAt(i).getCoordY(), pointStart)) {
                            indexNodMutat = i;
                        }
                    }
                }
            }

            //evenimentul care se produce la eliberarea mousse-ului
            public void mouseReleased(MouseEvent e) {
                if (!modMutareNoduri) {
                    if (!isDragging) {
                        //adaugam primul nod in lista
                        if (listaNoduri.size() == 0)
                            addNode(e.getX(), e.getY());
                        else
                            //verificam distanta intre punctele curente si fiecare nod din lista
                            if ((canAddNode(e.getX(), e.getY())))
                                addNode(e.getX(), e.getY());

                    } else {
                        //verificam daca pointStart este pe suprafata unui nod
                        for (int i = 0; i < listaNoduri.size(); i++) {
                            if (nodeSurface(listaNoduri.elementAt(i).getCoordX(), listaNoduri.elementAt(i).getCoordY(), pointStart)) {

                                for (int j = 0; j < listaNoduri.size(); j++) {
                                    if (nodeSurface(listaNoduri.elementAt(j).getCoordX(), listaNoduri.elementAt(j).getCoordY(), pointEnd)) {

                                        //daca ambele conditii sunt indeplinite si nodurile sunt diferite, se adauga arcul in listaArce
                                        if (i != j) {
                                            Arc arc = new Arc(listaNoduri.elementAt(i),listaNoduri.elementAt(j));
                                            listaArce.add(arc);

                                            if (grafOrientat) {

                                                //se adauga arcul in matricea de adiacenta
                                                matriceAdiacenta[i][j] = 1;
                                                matriceAdiacenta[j][i] = 0;
                                            } else {

                                                //se adauga arcele in matricea de adiacenta
                                                matriceAdiacenta[i][j] = 1;
                                                matriceAdiacenta[j][i] = 1;
                                            }
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
                    afisareMatriceAdiacenta(matriceAdiacenta);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            //evenimentul care se produce la drag&drop pe mousse
            public void mouseDragged(MouseEvent e) {
                pointEnd = e.getPoint();
                isDragging = true;
                if (!modMutareNoduri) {
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
    private void addNode(int x, int y) {
        Node node = new Node(x, y, nodeNr);
        listaNoduri.add(node);
        nodeNr++;
        repaint();
    }

    //se executa atunci cand apelam repaint()
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//apelez metoda paintComponent din clasa de baza

        //daca este graf orientat, se trag arce cu sageata in capete
        if (grafOrientat) {
            for (Arc a : listaArce) {
                a.drawArrow(g);
            }
            //deseneaza arcul curent; cel care e in curs de desenare
            if (pointStart != null && !modMutareNoduri) {
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
            if (pointStart != null && !modMutareNoduri) {
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
