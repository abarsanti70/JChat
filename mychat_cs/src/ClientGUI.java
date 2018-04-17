import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;

public class ClientGUI extends JFrame{

    private JPanel panel;
    private JTextArea textArea1;
    private final Border padding=BorderFactory.createEmptyBorder(10, 10, 10, 10);

    public ClientGUI(){

        setTitle("MyChat");
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel.setBorder(padding);

        add(panel);
        setVisible(true);

    }

    public static void main(String[] args){

        new ClientGUI();

    }

}
