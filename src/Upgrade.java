
import javax.swing.*;
import java.util.Random;

abstract public class Upgrade extends Thread{

    private final Random randGen = new Random();
    private final ImageIcon img;
    private final Cell[][] cells;
    private int row, col;
    protected boolean isActive = true;
    protected String path;

    protected Board board;

    public Upgrade(Board b, Upgrades type){
        board = b;
        cells = b.getCells();
        path = type.toString();
        img = new ImageIcon(path);
        generatePosition();
        start();
    }

    public boolean isCellRunnable(int row, int col){
        return (cells[row][col].getType() == '0' || cells[row][col].getType() == 'p');
    }
    public int generateRow(){
        return randGen.nextInt(board.rows);
    }

    public int generateCol(){
        return randGen.nextInt(board.cols);
    }

    public void checkCollision(){
        if (row == board.pacman.getRow() && col == board.pacman.getCol()){
            upgrade();
            isActive = false;
            board.ups.remove(this);
        }
    }

    @Override
    public void run(){
        while (isActive){
            if (board.lives == 0){
                isActive = false;
                break;
            }
            checkCollision();
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        try {
            int activeTime = 5000;
            sleep(activeTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        restore();

    }
    public void generatePosition(){
        int tempRow = generateRow();
        int tempCol = generateCol();
        while (!isCellRunnable(tempRow, tempCol)){
            tempRow = generateRow();
            tempCol = generateCol();
        }
        col = tempCol;
        row = tempRow;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }


    public void draw(){
        board.add(new JLabel(img));
    }

    abstract void upgrade();
    abstract void restore();


}
