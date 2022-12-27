import javax.swing.*;

public class ButtonFrame extends JFrame {
    static public String type="";
    JButton orientat, neorientat;
    void orientat(){
        type="Orientat";
        this.dispose();
        new Graf();
    }
    void neorientat(){
        type="Neorientat";
        this.dispose();
        new Graf();
    }
    public ButtonFrame() {
        orientat=new JButton("Orientat");
        orientat.setBounds(120,150,100,50);
        orientat.addActionListener(e->orientat());

        neorientat=new JButton("Neorientat");
        neorientat.setBounds(320,150,100,50);
        neorientat.addActionListener(e->neorientat());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(500,500);
        setVisible(true);
        add(orientat);
        add(neorientat);
    }
}
