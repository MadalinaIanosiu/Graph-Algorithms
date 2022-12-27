import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;

public class Graf
{
	private static void initUI() {
        JFrame f = new JFrame("Algoritmica Grafurilor");

        MyPanel myPanel = new MyPanel();

        JPanel creeareGrafPanel = new JPanel();
        creeareGrafPanel.setLayout(null);

        //creez panel-ul pentru introducere nodurilor
        JPanel textBoxPanel = new JPanel();
        textBoxPanel.setLayout(null);

        Button button = new Button("START");
        button.setBounds(420,280,100,50);
        button.setForeground(Color.black.darker());
        creeareGrafPanel.add(button);

        f.add(creeareGrafPanel);


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == button) {
                    creeareGrafPanel.setVisible(false);
                    f.add(myPanel);
                }
            }
        } );

        //la inchiderea fetestrei se face soartarea topologica
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if(myPanel.isCyclic() == false) {
                    System.out.println("Sortarea topologica este: ");
                    myPanel.topologicalSort();
                    System.exit(0);
                }
                else {
                   System.out.println("Graful nu este aciclic");
                }
                System.exit(0);
            }
        });

        //setez dimensiunea ferestrei
        f.setSize(900, 800);
        //fac fereastra vizibila
        f.setVisible(true);
    }

    public static void main(String[] args)
	{
		//pornesc firul de executie grafic
		//fie prin implementarea interfetei Runnable, fie printr-un ob al clasei Thread
		SwingUtilities.invokeLater(new Runnable() //new Thread()
		{
            public void run() 
            {
            	initUI();
            }
        });
	}
}
