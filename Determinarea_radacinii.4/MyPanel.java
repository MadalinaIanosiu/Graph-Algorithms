import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;


public class MyPanel extends JPanel {
    int INT_MAX=999;
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
    int indexNodMutat =0;
    int possibleRoot=0; //reprezinta nodul cu t2 maxim

    //initializare matrice cu valori de 0
    void initMatrice(int[][] matriceAdiacenta) {
        for (int index = 0; index < listaNoduri.size(); index++) {
            for (int index2 = 0; index2 < listaNoduri.size(); index2++) {
                matriceAdiacenta[index][index2] = 0;
            }
        }
    }

    boolean find(Integer x,Vector<Integer> v)
    {
        for(int i=0;i<v.size();i++)
            if(v.elementAt(i)==x)return true;
        return false;
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

    // functie folosita pt a verifica daca coordonatele mouse-ului se afla pe surpafata unui nod
    boolean nodeSurface(int x, int y, Point p) {
        return (((x < p.getX()) && (p.getX() < x + nodeDiam)) && ((y < p.getY()) && (p.getY() < y + nodeDiam)));
    }

    boolean isCyclic(int i, boolean[] visited, boolean[] recStack)
    {// Mark the current node as visited and
        // part of recursion stack
        if (recStack[i])  return true;
        if (visited[i]) return false;
        visited[i] = true;
        recStack[i] = true;
        Vector<Integer> children=new Vector<>();
        for(int j=0;j<listaNoduri.size();j++)
        {
            if(matriceAdiacenta[i][j]==1)
            {
                Integer J=j;
                children.add(J);
            }
        }

        for (Integer c: children)
            if (isCyclic(c, visited, recStack))
                return true;
        recStack[i] = false;
        return false;
    }

    boolean programDF(Integer s)//returneaza fals daca din nodul cu t2 maxim nu pot ajunge in totate noduri
    {
        Vector<Integer> u=new Vector<Integer>();
        for(int i=0;i<listaNoduri.size();i++)
        {
            if(i!=s) {
                u.add(i);
            }
        }
        Vector<Integer> v= new Vector<Integer>();
        v.add(s);
        Vector<Integer> w=new Vector<Integer>();
        int p[]=new int[listaNoduri.size()];
        int t1[]=new int[listaNoduri.size()];
        int t2[]=new int[listaNoduri.size()];
        int t=1;
        for(int y=0;y<listaNoduri.size();y++)
        {
            p[y]=0;
            t1[y]=INT_MAX;
            t2[y]=INT_MAX;
        }
        t1[s]=t;
        int it=0;
        Integer y;
            while(v.size()!=0) {
                Integer x = v.elementAt(v.size() - 1);
                boolean ok=false;
                for (int i = 0; i < listaNoduri.size() && !ok; i++) {
                    if (matriceAdiacenta[x][i] == 1 && find(i,u)){
                        {
                            y = i; u.remove(y);
                            v.add(y); p[i] = x;
                            t1[i] = ++t; ok = true;
                        }
                    }
                }  if (!ok) {
                    v.remove(x); w.add(x);
                    t2[x] = ++t;
                }
            }
            if(w.size()==listaNoduri.size())
                return true;
            else
                return false;
    }

    int noOfCC(Integer s)//return false if contains more connected components
    {
        Vector<Integer> ST=new Vector<>();
        Vector<Integer> u=new Vector<Integer>();
        for(int i=1;i<listaNoduri.size();i++)
        {
            u.add(i);
        }
        Vector<Integer> v= new Vector<Integer>();
        v.add(s);
        Vector<Integer> w=new Vector<Integer>();
        int noCC=1;
        int p[]=new int[listaNoduri.size()];
        int t1[]=new int[listaNoduri.size()];
        int t2[]=new int[listaNoduri.size()];
        int t=1;
        for(int y=0;y<listaNoduri.size();y++)
        {
            p[y]=0;
            t1[y]=INT_MAX;
            t2[y]=INT_MAX;
        }
        t1[s]=t;
        int it=0;
        Integer y;
        while(w.size()!=listaNoduri.size())
        {
            while(v.size()!=0) {
                Integer x = v.elementAt(v.size() - 1);
                boolean ok=false;
                for (int i = 0; i < listaNoduri.size() && !ok; i++) {
                    if ((matriceAdiacenta[x][i] == 1 ||matriceAdiacenta[i][x]== 1) && find(i,u)){
                        {
                            y = i; u.remove(y);
                            v.add(y); p[i] = x;
                            t=t+1; t1[i] = t;
                            ok = true;
                        }
                    }
                }  if (!ok) {
                    v.remove(x); w.add(x);
                    t=t+1; t2[x] = t;
                    ST.add(0,x);
                }
            }
            if(u.size()!=0)
            {
                s=u.elementAt(0); v.clear();
                v.add(s); u.remove(s);
                t1[s]=++t;
                noCC=noCC+1;
            }
        }

        return noCC;
    }

    public MyPanel() {
        listaNoduri = new Vector<Node>();
        listaArce = new Vector<Arc>();
        initMatrice(matriceAdiacenta);

        // borderul panel-ului
        setBorder(BorderFactory.createLineBorder(Color.black));

        //creez buton pentru modul de desenare graf orientat
        MyPanel f = this;
        JToggleButton b = new JToggleButton("Graf orientat");
        b.setBounds(60, 100, 95, 30);
        f.add(b);
        b.setVisible(true);

        //creez buton pentru modul mutare noduri
        JToggleButton b1 = new JToggleButton("Repozitionare graf ");
        b1.setBounds(30, 100, 95, 30);
        f.add(b1);
        b1.setVisible(true);

        //creez buton pentru afisarea nodurilor in ordine topologica
        JToggleButton b2=new JToggleButton("Afla radacina");
        b2.setBounds(0,100,95,30);
        f.add(b2);
        b2.setVisible(true);

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

        //afisez radacina grafului daca se poate
        ActionListener actionListener2 = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton abstractButton2 = (AbstractButton) actionEvent.getSource();
                boolean selected2 = abstractButton2.getModel().isSelected();
                if (selected2 && grafOrientat) {
                    boolean [] visited=new boolean[listaNoduri.size()];
                    boolean [] recStack=new boolean[listaNoduri.size()];
                    if(isCyclic(0,visited,recStack))
                        System.out.println("Graful nu e o arborescenta)1)");
                    if(noOfCC(0)!=1)
                        System.out.println("Graful nu e o arborescenta)2)");
                    if(programDF(possibleRoot)==true)
                        System.out.println("Radacina grafului este nodul  " + possibleRoot);
                    else
                        System.out.println("Graful curent nu are radacina");
                }
                else{
                    System.out.println("");
                }
            }
        };
        b2.addActionListener(actionListener2);

        addMouseListener(new MouseAdapter() {
            //evenimentul care se produce la apasarea mousse-ului
            public void mousePressed(MouseEvent e) {
                pointStart = e.getPoint();
                if (modMutareNoduri) {
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
                if (!modMutareNoduri) {
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
                                            matriceAdiacenta[i][j]=1;
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
