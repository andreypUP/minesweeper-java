package game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Board extends JPanel{

    private final int numberOfImage = 13;
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


    private final int numberOfMines = 40;
    private final int numberOfRows = 16;
    private final int numberOfColumns = 16;

    private final int boardWidth = numberOfColumns * cellSize + 1;
    private final int boardHeight = numberOfRows * cellSize + 1;


    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] images;
    private int allCells;
    private final JLabel status;
    private int clickCounter;
    private int locationOfFirstClickedCell;

    public Board(JLabel status) {

        this.status = status;
        initBoard();


    }

    private void initBoard() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        images = new Image[numberOfImage];

        for (int i = 0; i < numberOfImage; i++) {
            var path = "src/resources/" + i + ".png";
            images[i] = (new ImageIcon(path)).getImage();
        }
        newGame();
        addMouseListener(new MinesAdapter());
    }

    private void newGame() {
        clickCounter = 0;
        inGame = true;                  //GAME IN PROGRESS
        minesLeft = numberOfMines;

        allCells = numberOfRows * numberOfColumns;         //TOTAL CELL ON THE BOARD
        field = new int[allCells];      //STORE THE STATE OF EACH CELL

        for (int i = 0; i < allCells; i++) {

            field[i] = cellCover;           //COVERS ALL CELL
        }

        status.setText(Integer.toString(minesLeft));       //DISPLAY MINES LEFT

    }
    private void findEmptyCell(int clickedCell) {

        int current_col = clickedCell % numberOfColumns;
        int cell;

        if (current_col > 0) {
            cell = clickedCell - numberOfColumns - 1;
            if (cell >= 0) {
                emptyCellChecker(cell);
            }

            cell = clickedCell - 1;
            if (cell >= 0) {
                emptyCellChecker(cell);
            }

            cell = clickedCell + numberOfColumns - 1;
            if (cell < allCells) {
                emptyCellChecker(cell);
            }
        }

        cell = clickedCell - numberOfColumns; //SUBTRACT LEFT AND COLUMN = TOP OF LEFT (TOP-LEFT OF ORIGINAL CLICKED BOX)
        if (cell >= 0) {
            emptyCellChecker(cell);
        }

        cell = clickedCell + numberOfColumns; //ADD LEFT AND COLUMN
        if (cell < allCells) {
            emptyCellChecker(cell);
        }

        if (current_col < (numberOfColumns - 1)) {
            cell = clickedCell - numberOfColumns + 1;
            if (cell >= 0) {
                emptyCellChecker(cell);
            }

            cell = clickedCell + numberOfColumns + 1;
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

        for (int i = 0; i < numberOfRows; i++) { //UP TO ROWS (HORIZONTALLY)

            for (int j = 0; j < numberOfColumns; j++) { //UP TO COLUMN (VERTICALLY)

                int cell = field[(i * numberOfColumns) + j];

                if (inGame && cell == cellMine) { //IN GAME AND CLICKED MINE

                    inGame = false; //STOP GAME
                }

                if (!inGame) {

                    if (cell == coveredCellMine) {
                        cell = drawMine; //REVEAL ALL MINES AFTER inGame == FALSE
                    } else if (cell == flaggedCellMine) {
                        cell = drawFlag; //SHOW IF FLAG IS CORRECT
                    } else if (cell > coveredCellMine) {
                        cell = drawWrongFlag; //SHOW IF FLAG IS WRONG
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

                g.drawImage(images[cell], (j * cellSize), (i * cellSize), this);
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

            if ((x < numberOfColumns * cellSize) && (y < numberOfRows * cellSize)) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (field[(cRow * numberOfColumns) + cCol] > cellMine) {

                        doRepaint = true;

                        if (field[(cRow * numberOfColumns) + cCol] <= coveredCellMine) {

                            if (minesLeft > 0) {
                                field[(cRow * numberOfColumns) + cCol] += cellFlag;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                status.setText(msg);
                            } else {
                                status.setText("No marks left");
                            }
                        } else {

                            field[(cRow * numberOfColumns) + cCol] -= cellFlag;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            status.setText(msg);
                        }
                    }

                } else {
                    clickCounter++;
                    if(clickCounter == 1 && inGame) {
                        locationOfFirstClickedCell = (cRow * numberOfColumns) + cCol;
                        //System.out.println(loc);
                        generateMine();
                    }

                    if (field[(cRow * numberOfColumns) + cCol] > coveredCellMine) {
                        return;
                    }

                    if ((field[(cRow * numberOfColumns) + cCol] > cellMine)
                            && (field[(cRow * numberOfColumns) + cCol] < flaggedCellMine)) {

                        field[(cRow * numberOfColumns) + cCol] -= cellCover;
                        doRepaint = true;

                        if (field[(cRow * numberOfColumns) + cCol] == cellMine) {
                            inGame = false;
                        }

                        if (field[(cRow * numberOfColumns) + cCol] == cellEmpty) {
                            findEmptyCell((cRow * numberOfColumns) + cCol); //UPDATE CLICKEDCELL
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

        while (i < numberOfMines) {
            //nextInt() would produce a specified range making nextDouble() more precise.
            int minesPosition = 0;
            if(clickCounter == 1) {
                minesPosition = (int) (allCells * random.nextDouble()); //UNIFORM DISTRIBUTION OF RANDOM POSITION OF MINES
            }
            if ((minesPosition < allCells) && (field[minesPosition] != coveredCellMine) && minesPosition != locationOfFirstClickedCell) { //ENSURE MINE POSITION TO BE IN THE CELL RANGE
                //System.out.println();
                //System.out.println("i: " + i + "position: " + minesPosition);
                int current_col = minesPosition % numberOfColumns; ////COLUMN INDEX OF THE CELL
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
            cell = minePosition - 1 - numberOfColumns;                   //ADJACENT CELL IN THE UPPER LEFT POSITION
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

            cell = minePosition + numberOfColumns - 1;                     //ADJACENT CELL IN THE LOWER LEFT POSITION

            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
        }
        //UPDATE THE MINE COUNTS FOR ADJACENT ABOVE AND BELOW(RESPECTIVELY) OF THE CURRENT POSITION
        cell = minePosition - numberOfColumns;

        if (cell >= 0) {
            if (field[cell] != coveredCellMine) {
                field[cell] += 1;
            }
        }

        cell = minePosition + numberOfColumns;
        if (cell < allCells) {
            if (field[cell] != coveredCellMine) {
                field[cell] += 1;
            }
        }
        //UPDATE THE MINE COUNTS FOR ADJACENT CELL TO THE RIGHT OF THE CURRENT POSITION
        if (current_col < (numberOfColumns - 1)) {         //CHECK CURRENT POSITION IS NOT OUTSIDE BOARD
            cell = minePosition - numberOfColumns + 1;         //ADJACENT CELL IN THE UPPER RIGHT POSITION
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
            cell = minePosition + numberOfColumns + 1;             //ADJACENT CELL IN THE LOWER RIGHT POSITION
            if (cell < allCells) {
                if (field[cell] != coveredCellMine) {
                    field[cell] += 1;
                }
            }
        }
    }
}
