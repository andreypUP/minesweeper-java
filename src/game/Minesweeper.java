package game;


import java.awt.*;
import javax.swing.*;

public class Minesweeper extends JFrame {
    private JLabel status;

    public Minesweeper() {

        ImageIcon icon = new ImageIcon("src//resources//13.png");

        status = new JLabel("Status");
        add(status, BorderLayout.SOUTH);
        add(new Board(status));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}