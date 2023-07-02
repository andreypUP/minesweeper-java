package game;

import java.awt.*;
import javax.swing.*;

public class Minesweeper extends JFrame {
    private JLabel status;
    private GUI gui;


    public Minesweeper() {

        ImageIcon icon = new ImageIcon("src//resources//9.png");

        status = new JLabel("");
        add(status, BorderLayout.SOUTH);
        add(new Board(status, gui));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper();
        ms.setVisible(true);
    }
}
