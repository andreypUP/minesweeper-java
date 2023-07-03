package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class Board extends JPanel{

    private final int numOfImages = 13;
    private final int cellSize = 15;

    private final int cellCover = 10;
    private final int cellFlag = 10;
    private final int cellEmpty = 0;
    private final int cellMine = 9;
    private final int coveredCellMine = cellMine + cellCover;
    private final int flaggedCellMine = coveredCellMine + cellFlag;

    private final int drawMine = 9;
    private final int drawCover = 10;
    private final int drawFlag = 11;
    private final int drawWrongFlag = 12;


    private final int numbOfMines = 10;
    private final int rows = 6;
    private final int cols = 6;

    private final int boardWidth = cols * cellSize + 1;
    private final int boardHeight = rows * cellSize + 1;


    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] img;
    private int allCells;
    private final JLabel status;


    public Board(JLabel status) {

        this.status = status;
        initBoard();


    }

    private void initBoard() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        img = new Image[numOfImages];

        for (int i = 0; i < numOfImages; i++) {
            var path = "src/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void newGame() {


        inGame = true;                  //GAME IN PROGRESS
        minesLeft = numbOfMines;

        allCells = rows * cols;         //TOTAL CELL ON THE BOARD
        field = new int[allCells];      //STORE THE STATE OF EACH CELL

        for (int i = 0; i < allCells; i++) {

            field[i] = cellCover;           //COVERS ALL CELL
        }

        status.setText(Integer.toString(minesLeft));       //DISPLAY MINES LEFT
        
        generateMine();                                     //METHOD FOR GENERATING MINE

        
    }
    private void findEmptyCell(int clickedCell) {

        int current_col = clickedCell % cols;
        int cell;

        if (current_col > 0) {
            cell = clickedCell - cols - 1;
            if (cell >= 0) {
                doFindEmptyCell(cell);
            }

            cell = clickedCell - 1;
            if (cell >= 0) {
                doFindEmptyCell(cell);
            }

            cell = clickedCell + cols - 1;
            if (cell < allCells) {
                doFindEmptyCell(cell);
            }
        }

        cell = clickedCell - cols;
        if (cell >= 0) {
            doFindEmptyCell(cell);
        }

        cell = clickedCell + cols;
        if (cell < allCells) {
            doFindEmptyCell(cell);
        }

        if (current_col < (cols - 1)) {
            cell = clickedCell - cols + 1;
            if (cell >= 0) {
                doFindEmptyCell(cell);
            }

            cell = clickedCell + cols + 1;
            if (cell < allCells) {
                doFindEmptyCell(cell);
            }

            cell = clickedCell + 1;
            if (cell < allCells) {
                doFindEmptyCell(cell);
            }
        }

    }


    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                int cell = field[(i * cols) + j];

                if (inGame && cell == cellMine) {

                    inGame = false;
                }

                if (!inGame) {

                    if (cell == coveredCellMine) {
                        cell = drawMine;
                    } else if (cell == flaggedCellMine) {
                        cell = drawFlag;
                    } else if (cell > coveredCellMine) {
                        cell = drawWrongFlag;
                    } else if (cell > cellMine) {
                        cell = drawCover;
                    }

                } else {

                    if (cell > coveredCellMine) {
                        cell = drawFlag;
                    } else if (cell > cellMine) {
                        cell = drawCover;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * cellSize),
                        (i * cellSize), this);
            }
        }

        if (uncover == 0 && inGame) {

            inGame = false;
            status.setText("You won!");

        } else if (!inGame) {
            status.setText("Game Over!");
        }
    }
    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / cellSize;
            int cRow = y / cellSize;

            boolean doRepaint = false;

            if (!inGame) {

                newGame();
                repaint();
            }

            if ((x < cols * cellSize) && (y < rows * cellSize)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * cols) + cCol] > cellMine) {

                        doRepaint = true;

                        if (field[(cRow * cols) + cCol] <= coveredCellMine) {

                            if (minesLeft > 0) {
                                field[(cRow * cols) + cCol] += cellFlag;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                status.setText(msg);
                            } else {
                                status.setText("No marks left");
                            }
                        } else {

                            field[(cRow * cols) + cCol] -= cellFlag;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            status.setText(msg);
                        }
                    }

                } else {

                    if (field[(cRow * cols) + cCol] > coveredCellMine) {

                        return;
                    }

                    if ((field[(cRow * cols) + cCol] > cellMine)
                            && (field[(cRow * cols) + cCol] < flaggedCellMine)) {

                        field[(cRow * cols) + cCol] -= cellCover;
                        doRepaint = true;

                        if (field[(cRow * cols) + cCol] == cellMine) {
                            inGame = false;
                        }

                        if (field[(cRow * cols) + cCol] == cellEmpty) {
                            findEmptyCell((cRow * cols) + cCol);
                        }
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }

    public void doFindEmptyCell(int cell) {
        if (field[cell] > cellMine) {
            field[cell] -= cellCover;
            if (field[cell] == cellEmpty) {
                findEmptyCell(cell);
            }
        }
    }
    
    public void generateMine() {


        var random = new Random(); //NEW INSTANCE FOR GENERATING RANDOM NUMBER
        int i = 0;

        while (i < numbOfMines) {
            //nextInt() would produce a specified range making nextDouble() more precise.
            int position = (int) (allCells * random.nextDouble()); //UNIFORM DISTRIBUTION OF RANDOM POSITION OF MINES

            if ((position < allCells) && (field[position] != coveredCellMine)) { //ENSURE MINE POSITION TO BE IN THE CELL RANGE

                int current_col = position % cols; ////COLUMN INDEX OF THE CELL
                field[position] = coveredCellMine;
                /*
                 *For example, if position is 7 and cols is 4,
                 *  then position % cols will be 3, indicating that the cell
                 * is in the third column of the field.
                 * */
                checkNeighbor(current_col,position);
                i++;
                //UPDATE THE MINE COUNTS FOR ADJACENT CELL TO THE LEFT OF THE CURRENT POSITION
            }
        }
    }
    public void checkNeighbor(int current_col, int position) {
        int cell;
        if (current_col > 0) {
            cell = position - 1 - cols  ;                   //ADJACENT CELL IN THE UPPER LEFT POSITION
            if (cell >= 0) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
            cell = position - 1;                            //ADJACENT CELL IN THE LEFT POSITION
            if (cell >= 0) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }

            cell = position + cols  - 1;                     //ADJACENT CELL IN THE LOWER LEFT POSITION

            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
        }
        //UPDATE THE MINE COUNTS FOR ADJACENT ABOVE AND BELOW(RESPECTIVELY) OF THE CURRENT POSITION
        cell = position - cols;

        if (cell >= 0) {
            if (field[cell] != coveredCellMine) {
                field[cell] += 1;
            }
        }

        cell = position + cols;
        if (cell < allCells) {
            if (field[cell] != coveredCellMine) {
                field[cell] += 1;
            }
        }
        //UPDATE THE MINE COUNTS FOR ADJACENT CELL TO THE RIGHT OF THE CURRENT POSITION
        if (current_col < (cols - 1)) {         //CHECK CURRENT POSITION IS NOT OUTSIDE BOARD
            cell = position - cols + 1;         //ADJACENT CELL IN THE UPPER RIGHT POSITION
            if (cell >= 0) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
            cell = position + 1;                    //ADJACENT CELL IN THE RIGHT POSITION
            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
            cell = position + cols + 1;             //ADJACENT CELL IN THE LOWER RIGHT POSITION
            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
        }
    }
}
