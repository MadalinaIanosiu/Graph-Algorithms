import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Graf {
    public static int width=1100,height=650;
    public Graf() {
    }

    private static void initUI() {
        JFrame f = new JFrame("Algoritmica Grafurilor");
        f.setDefaultCloseOperation(3);
        f.add(new MyPanel());
        f.setSize(width, height);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Graf.initUI();
            }
        });
    }
}
