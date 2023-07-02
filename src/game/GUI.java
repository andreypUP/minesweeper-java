package game;

import javax.swing.*;
import java.awt.*;

public class GUI extends JPanel {

    private Board board;
    boolean process;
    private final int drawMine = 9;
    private final int drawCover = 10;
    private final int drawFlag = 11;
    private final int drawWrongFlag = 12;

    public GUI(Board board) {
       this.board = board;
    }
    @Override
    public void paintComponent(Graphics g) {
        System.out.println("Painting GUI.");
        super.paintComponent(g);
        int uncover = 0;
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                int cell = board.getField()[(i * board.getCols()) + j];
                if (board.getInGame() && cell == board.getCellMine()) {
                    process = board.setInGame(false);
                }
                if (!process) {
                    if (cell == board.getCoveredCellMine()) {
                        cell = drawMine;
                    } else if (cell == board.getFlaggedCellMine()) {
                        cell = drawWrongFlag;
                    } else if (cell > board.getCellMine()) {
                        cell = drawCover;
                    }
                } else {
                    if (cell > board.getCoveredCellMine()) {
                        cell = drawFlag;
                    } else if (cell > board.getCellMine()) {
                        cell = drawCover;
                        uncover++;
                    }
                }
                g.drawImage(board.getImg()[cell], (j * board.getCellSize()), (i * board.getCellSize()), this);
            }
        }
        if (uncover == 0 && board.getInGame()) {
            board.setInGame(false);
            board.getStatus().setText("You Won!");
        } else if (!board.getInGame()) {
            board.getStatus().setText("Game Over!");
        }
    }


}


