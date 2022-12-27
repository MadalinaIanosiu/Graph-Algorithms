import javax.swing.*;

public class Main {
    private static void initUI() throws Exception{
        JFrame frame = new JFrame("Labirint");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(1920, 1080);

        Panel panel = new Panel("Matrice.txt");
        panel.readMatrix();
        panel.initGraph();
        panel.setStartNode();

        frame.add(panel);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    initUI();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

