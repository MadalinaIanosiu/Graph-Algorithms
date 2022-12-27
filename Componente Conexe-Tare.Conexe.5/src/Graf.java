import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.Objects;

public class Graf {
    static String type = "";
    static int width = 1000;
    static int height = 1000;

    public Graf() {
        if (Objects.equals(ButtonFrame.type, "Orientat")) {
            type = "Componente tari conexe";
        } else if (Objects.equals(ButtonFrame.type, "Neorientat")) {
            type = "Componente conexe";
        }
        JFrame f = new JFrame(type);
        f.setDefaultCloseOperation(3);
        f.add(new MyPanel());
        f.setSize(width, height);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new ButtonFrame();
    }
}
