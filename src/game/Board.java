package game;

import java.awt.*;
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


    private final int numbOfMines = 254;
    private final int rows = 16;
    private final int cols = 16;

    private final int boardWidth = cols * cellSize + 1;
    private final int boardHeight = rows * cellSize + 1;


    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] img;
    private int allCells;
    private final JLabel status;
    private int counter;
    private int loc;

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
        newGame();
        addMouseListener(new MinesAdapter());
    }

    private void newGame() {
        counter = 0;
        inGame = true;                  //GAME IN PROGRESS
        minesLeft = numbOfMines;

        allCells = rows * cols;         //TOTAL CELL ON THE BOARD
        field = new int[allCells];      //STORE THE STATE OF EACH CELL

        for (int i = 0; i < allCells; i++) {

            field[i] = cellCover;           //COVERS ALL CELL
        }

        status.setText(Integer.toString(minesLeft));       //DISPLAY MINES LEFT

    }
    private void findEmptyCell(int clickedCell) {

        int current_col = clickedCell % cols;
        int cell;

        if (current_col > 0) {
            cell = clickedCell - cols - 1;
            if (cell >= 0) {
                emptyCellChecker(cell);
            }

            cell = clickedCell - 1;
            if (cell >= 0) {
                emptyCellChecker(cell);
            }

            cell = clickedCell + cols - 1;
            if (cell < allCells) {
                emptyCellChecker(cell);
            }
        }

        cell = clickedCell - cols; //SUBTRACT LEFT AND COLUMN = TOP OF LEFT (TOP-LEFT OF ORIGINAL CLICKED BOX)
        if (cell >= 0) {
            emptyCellChecker(cell);
        }

        cell = clickedCell + cols; //ADD LEFT AND COLUMN
        if (cell < allCells) {
            emptyCellChecker(cell);
        }

        if (current_col < (cols - 1)) {
            cell = clickedCell - cols + 1;
            if (cell >= 0) {
                emptyCellChecker(cell);
            }

            cell = clickedCell + cols + 1;
            if (cell < allCells) {
                emptyCellChecker(cell);
            }

            cell = clickedCell + 1;
            if (cell < allCells) {
                emptyCellChecker(cell);
            }
        }

    }


    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < rows; i++) { //UP TO ROWS (HORIZONTALLY)

            for (int j = 0; j < cols; j++) { //UP TO COLUMN (VERTICALLY)

                int cell = field[(i * cols) + j];

                if (inGame && cell == cellMine) { //IN GAME AND CLICKED MINE

                    inGame = false; //STOP GAME
                }

                if (!inGame) {

                    if (cell == coveredCellMine) {
                        cell = drawMine; //REVEAL ALL MINES AFTER IN GAME = FALSE
                    } else if (cell == flaggedCellMine) {
                        cell = drawFlag; //SHOW IF FLAG IS CORRECT
                    } else if (cell > coveredCellMine) {
                        cell = drawWrongFlag; //SHOW IF FLAG IS WRONG OR FLAGGED MINE HAS A MINE
                    } else if (cell > cellMine) {
                        cell = drawCover; //UNCOVERED EMPTY CELL REMAINS UNCOVERED
                    }

                } else { //GAME IN PROGRESS

                    if (cell > coveredCellMine) { //FLAGGED ALL POSSIBLE BOXES (EMPTY CELL -> TO MINES)
                        cell = drawFlag;
                    } else if (cell > cellMine) {
                        cell = drawCover;
                        uncover++;
                       // System.out.println(uncover);
                    }
                }

                g.drawImage(img[cell], (j * cellSize), (i * cellSize), this);
            }
        }

        if (uncover == 0 && inGame) {

            inGame = false;
            status.setText("You won!");

        } else if (!inGame) {
            status.setText("Game Over!");
            Toolkit.getDefaultToolkit().beep();
        }
    }
    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();
            System.out.println(x);
            System.out.println(y);
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
                    counter++;
                    if(counter == 1 && inGame) {
                        loc = (cRow * cols) + cCol;
                        //System.out.println(loc);
                        generateMine();
                    }

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
                            findEmptyCell((cRow * cols) + cCol); //UPDATE CLICKEDCELL
                        }
                    }
                }
                //System.out.println(counter);
                if (doRepaint) {
                    repaint();
                }
            }
        }
    }

    public void emptyCellChecker(int cell) {
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
            int minesPosition = 0;
            if(counter == 1) {
                minesPosition = (int) (allCells * random.nextDouble()); //UNIFORM DISTRIBUTION OF RANDOM POSITION OF MINES
            }
            if ((minesPosition < allCells) && (field[minesPosition] != coveredCellMine) && minesPosition != loc) { //ENSURE MINE POSITION TO BE IN THE CELL RANGE
                //System.out.println();
                //System.out.println("i: " + i + "position: " + minesPosition);
                int current_col = minesPosition % cols; ////COLUMN INDEX OF THE CELL
                field[minesPosition] = coveredCellMine;
                /*
                 *For example, if position is 7 and cols is 4,
                 *  then position % cols will be 3, indicating that the cell
                 * is in the third column of the field.
                 * */
                checkNeighbor(current_col, minesPosition);
                i++;
                //UPDATE THE MINE COUNTS FOR ADJACENT CELL TO THE LEFT OF THE CURRENT POSITION
            }
        }
    }
    public void checkNeighbor(int current_col, int minePosition) {
        int cell;
        if (current_col > 0) {
            cell = minePosition - 1 - cols  ;                   //ADJACENT CELL IN THE UPPER LEFT POSITION
            if (cell >= 0) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
            cell = minePosition - 1;                            //ADJACENT CELL IN THE LEFT POSITION
            if (cell >= 0) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }

            cell = minePosition + cols  - 1;                     //ADJACENT CELL IN THE LOWER LEFT POSITION

            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
        }
        //UPDATE THE MINE COUNTS FOR ADJACENT ABOVE AND BELOW(RESPECTIVELY) OF THE CURRENT POSITION
        cell = minePosition - cols;

        if (cell >= 0) {
            if (field[cell] != coveredCellMine) {
                field[cell] += 1;
            }
        }

        cell = minePosition + cols;
        if (cell < allCells) {
            if (field[cell] != coveredCellMine) {
                field[cell] += 1;
            }
        }
        //UPDATE THE MINE COUNTS FOR ADJACENT CELL TO THE RIGHT OF THE CURRENT POSITION
        if (current_col < (cols - 1)) {         //CHECK CURRENT POSITION IS NOT OUTSIDE BOARD
            cell = minePosition - cols + 1;         //ADJACENT CELL IN THE UPPER RIGHT POSITION
            if (cell >= 0) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
            cell = minePosition + 1;                    //ADJACENT CELL IN THE RIGHT POSITION
            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
            cell = minePosition + cols + 1;             //ADJACENT CELL IN THE LOWER RIGHT POSITION
            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
        }
    }
}
