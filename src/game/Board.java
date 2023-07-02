package game;
import java.awt.Dimension;
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
    private final int cellEmpty = 9;
    private final int cellMine = 9;
    private final int coveredCellMine = cellMine + cellCover;
    private final int flaggedCellMine = coveredCellMine + cellFlag;


    private final int numbOfMines = 40;
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
    private GUI gui;

    public Board(JLabel status, GUI gui) {

        this.status = status;
        initBoard();
        this.gui = gui;


    }

    private void initBoard() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        img = new Image[numOfImages];

        for (int i = 0; i <numOfImages ; i++) {
            var path = "src/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());

        GUI gui = new GUI(this);
        add(gui);
        System.out.println("Board initialized.");
        newGame();
    }

    public void newGame() {
        System.out.println("Starting a new game.");
        int cell;
        int i = 0;

        var random = new Random(); //NEW INSTANCE FOR GENERATING RANDOM NUMBER
        inGame = true; //GAME IN PROGRESS
        minesLeft = numbOfMines;

        allCells = rows * cols; //TOTAL CELL ON THE BOARD
        field = new int[allCells]; //STORE THE STATE OF EACH CELL

        for (i = 0; i < allCells; i++) {

            field[i] = coveredCellMine; //COVERS ALL CEL
        }

        status.setText(Integer.toString(minesLeft)); //DISPLAY MINES LEFT
        while(i < numbOfMines) {
            //nextInt() would produce a specified range making nextDouble() more precise.
            int minePosition = (int) (allCells * random.nextDouble()); //UNIFORM DISTRIBUTION OF RANDOM POSITION OF MINES

            if ((minePosition < allCells) && (field[minePosition] != coveredCellMine)) { //ENSURE MINE POSITION TO BE IN THE CELL RANGE
                int currentCol = minePosition % cols; //COLUMN INDEX OF THE CELL
                /*
                 *For example, if position is 7 and cols is 4,
                 *  then position % cols will be 3, indicating that the cell
                 * is in the third column of the field.
                 * */
                field[minePosition] = coveredCellMine;
                i++;

                //UPDATE THE MINE COUNTS FOR ADJACENT CELL TO THE LEFT OF THE CURRENT POSITION
                if(currentCol > 0) {
                    cell = minePosition - 1 - cols; //ADJACENT CELL IN THE UPPER LEFT POSITION
                    if(cell >= 0) {
                        if (field[cell] != coveredCellMine) {
                            field[cell] += 1;
                        }
                    }
                    cell = minePosition - 1; //ADJACENT CELl IN THE LEFT POSITION
                    if(cell >= 0) {
                        if(field[cell] != coveredCellMine) {
                            field[cell] += 1;
                        }
                    }
                    cell = minePosition + cols - 1; //ADJACENT CEL IN THE LOWER LEFT POSITION
                    if(cell < allCells) {
                        if(field[cell] != coveredCellMine) {
                            field[cell] += 1;
                        }
                    }
                }
                //UPDATE THE MINE COUNTS FOR ADJACENT ABOVE AND BELOW(RESPECTIVELY) OF THE CURRENT POSITION
                cell = minePosition - cols;
                if(cell >= 0) {
                    if(field[cell] != coveredCellMine) {
                        field[cell] += 1;
                    }
                }
                cell = minePosition + cols;
                if(cell <allCells) {
                    if(field[cell] != coveredCellMine) {
                        field[cell] += 1;
                    }
                }
                //UPDATE THE MINE COUNTS FOR ADJACENT CELL TO THE RIGHT OF THE CURRENT POSITION
                if(currentCol < (cols - 1)) { //CHECK CURRENT POSITION IS NOT OUTSIDE BOARD
                    cell = minePosition - cols + 1; //ADJACENT CELL IN THE UPPER RIGHT POSITION
                    if(cell >= 0) {
                        if(field[cell] != coveredCellMine) {
                            field[cell] += 1;
                        }
                    }
                    cell = minePosition + 1; //ADJACENT CELL IN THE RIGHT POSITION
                    if(cell < allCells) {
                        if(field[cell] != coveredCellMine) {
                            field[cell] += 1;
                        }
                    }
                    cell = minePosition + cols + 1; //ADJACENT CELL IN THE LOWER RIGHT POSITION
                    if(cell < allCells) {
                        if (field[cell] != coveredCellMine) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }
    private void findEmptyCell(int j) {
        int currentCol = j % cols;
        int cell;

        if (currentCol > 0) {
            cell = j - cols - 1;
            if(cell >= 0) {
                if(field[cell] > cellMine) {
                    field[cell] -= coveredCellMine;
                    if(field[cell] == cellEmpty) {
                        findEmptyCell(cell);
                    }
                }
            }
            cell = j - 1;
            if(cell >= 0) {
                if(field[cell] > cellMine) {
                    field[cell] -= coveredCellMine;
                    if(field[cell] == cellEmpty) {
                        findEmptyCell(cell);
                    }
                }
            }
            cell = j + cols - 1;
            if(cell < allCells) {
                if(field[cell] > cellMine) {
                    field[cell] -= coveredCellMine;
                    if(field[cell] == cellEmpty) {
                        findEmptyCell(cell);
                    }
                }
            }
            cell = j - cols;
            if(cell < allCells) {
                if (field[cell] > cellMine) {
                    field[cell] -= coveredCellMine;
                    if (field[cell] == cellEmpty) {
                        findEmptyCell(cell);
                    }
                }
            }
            cell = j + cols;
            if(cell >= 0) {
                if (field[cell] > cellMine) {
                    field[cell] -= coveredCellMine;
                    if (field[cell] == cellEmpty) {
                        findEmptyCell(cell);
                    }
                }
            }
            if(currentCol < (cols - 1)) {
                cell = j - cols + 1;
                if(cell >= 0) {
                    if(field[cell] > cellMine) {
                        field[cell] -= coveredCellMine;
                        if(field[cell] == cellEmpty) {
                            findEmptyCell(cell);
                        }
                    }
                }
                cell = j + cols + 1;
                if(cell < 0) {
                    if(field[cell] > cellMine) {
                        field[cell] -= coveredCellMine;
                        if(field[cell] == cellEmpty) {
                            findEmptyCell(cell);
                        }
                    }
                }
                cell = j + 1;
                if(cell < allCells) {
                    if(field[cell] > cellMine) {
                        field[cell] -= coveredCellMine;
                        if(field[cell] == cellEmpty) {
                            findEmptyCell(cell);
                        }
                    }
                }
            }
        }
    }

    public void repaintGUI() {
        if(gui != null) {
            gui.repaint();
        }
    }
    public class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            int cCol = x / cellSize;
            int cRow = y / cellSize;

            boolean doRepaint = false;

            if(!inGame) {
                newGame();
                repaintGUI();
            }
            if((x < cols * cellSize) && (y < rows * cellSize)) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    if(field[(cRow * cols) + cCol] > cellMine) {
                        doRepaint = true;
                        if(field[(cRow * cols) + cCol] <= coveredCellMine) {
                            if (minesLeft > 0) {
                                field[(cRow * cols) + cCol] += cellFlag;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                status.setText(msg);
                            } else {
                                status.setText("No flag left");
                            }
                        } else {
                            field[(cRow * cols) + cCol] -= cellFlag;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            status.setText(msg);
                        }
                    }
                } else {
                    if(field[(cRow * cols) + cCol] > coveredCellMine) {
                        return;
                    }
                    if((field[(cRow * cols) + cCol] > cellMine) && (field[(cRow * cols) + cCol] < flaggedCellMine)) {
                        field[(cRow * cols) + cCol] -= coveredCellMine;
                        doRepaint = true;
                        if(field[(cRow * cols) + cCol] == cellMine) {
                            inGame = false;
                        }
                        if(field[(cRow * cols) + cCol] == cellEmpty) {
                            findEmptyCell((cRow * cols + cCol));
                        }
                    }
                }
                if(doRepaint) {
                    repaintGUI();
                }
            }
        }
    }
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
    public int getCellSize() {
        return cellSize;
    }
    public int[] getField() {
        return field;
    }
    public boolean getInGame() {
        return inGame;
    }
    public boolean setInGame(boolean inGame) {
        this.inGame = inGame;
        return inGame;
    }
    public int getCellMine() {
        return cellMine;
    }
    public int getCoveredCellMine() {
        return coveredCellMine;
    }
    public int getFlaggedCellMine() {
        return flaggedCellMine;
    }
    public Image[] getImg() {
        return img;
    }
    public JLabel getStatus() {
        return status;
    }
}
