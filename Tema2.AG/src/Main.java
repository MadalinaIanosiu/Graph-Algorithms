import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Scanner;
import java.util.Random;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Introduceti numarul de noduri = ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        //generez fereastra f;
        JFrame f = new JFrame("Algoritmica Grafurilor");
        //sa se inchida aplicatia atunci cand inchid fereastra
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel p = new MyPanel();
        //imi creez obj MyPanel
        f.add(p);
        //setez dimensiunea ferestrei
        f.setSize(500, 500);
        //fac fereastra vizibila
        f.setVisible(true);

        Random rand = new Random();
        int limit = 400;
        for (int i = 0; i < n; i++) { // generam o pereche de numere x,y care reprezinta coord unui nod pe ecran
            int x = rand.nextInt(limit);
            int y = rand.nextInt(limit);
            boolean ok = true;
            for(int j=0;j<p.listaNoduri.size();j++)
            {
                if (Math.sqrt((Math.pow((p.listaNoduri.elementAt(j).getCoordX() - x), 2)) +
                        (Math.pow((p.listaNoduri.elementAt(j).getCoordY() - y), 2))) < 40) //verificam ca nodurile sa nu se suprapuna
                    ok=false;

            }
            if(ok==true)
                p.addNode(x, y);
        }

        for(int i=0;i<p.listaNoduri.size();i++)
        {
            for(int j=0;j<p.listaNoduri.size();j++)
            {
                int arce = rand.nextInt(limit); //probabilitate de 50% sa se genereze arc sau nu
                if(arce<=200)
                {
                    Arc arc=new Arc(p.listaNoduri.elementAt(i),p.listaNoduri.elementAt(j)); //variabila care reprez arc de la nodul i la nodul j
                    p.listaArce.add(arc);
                }
            }
        }
        // aparea eroarea graficul g nu e initializat, si asta a fost optiunea de initializare automata
        Graphics g = new Graphics() {
            @Override
            public Graphics create() {
                return null;
            }

            @Override
            public void translate(int x, int y) {

            }

            @Override
            public Color getColor() {
                return null;
            }

            @Override
            public void setColor(Color c) {

            }

            @Override
            public void setPaintMode() {

            }

            @Override
            public void setXORMode(Color c1) {

            }

            @Override
            public Font getFont() {
                return null;
            }

            @Override
            public void setFont(Font font) {

            }

            @Override
            public FontMetrics getFontMetrics(Font f) {
                return null;
            }

            @Override
            public Rectangle getClipBounds() {
                return null;
            }

            @Override
            public void clipRect(int x, int y, int width, int height) {

            }

            @Override
            public void setClip(int x, int y, int width, int height) {

            }

            @Override
            public Shape getClip() {
                return null;
            }

            @Override
            public void setClip(Shape clip) {

            }

            @Override
            public void copyArea(int x, int y, int width, int height, int dx, int dy) {

            }

            @Override
            public void drawLine(int x1, int y1, int x2, int y2) {

            }

            @Override
            public void fillRect(int x, int y, int width, int height) {

            }

            @Override
            public void clearRect(int x, int y, int width, int height) {

            }

            @Override
            public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

            }

            @Override
            public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

            }

            @Override
            public void drawOval(int x, int y, int width, int height) {

            }

            @Override
            public void fillOval(int x, int y, int width, int height) {

            }

            @Override
            public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

            }

            @Override
            public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

            }

            @Override
            public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

            }

            @Override
            public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {

            }

            @Override
            public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

            }

            @Override
            public void drawString(String str, int x, int y) {

            }

            @Override
            public void drawString(AttributedCharacterIterator iterator, int x, int y) {

            }

            @Override
            public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
                return false;
            }

            @Override
            public void dispose() {

            }
        };
        //p.paintComponent(g);
        p.repaint();
    }
}