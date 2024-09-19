import javax.swing.*;
import java.util.Random;

public class Ghost extends Thread{
    private static final String prefix = "assets/imgs/ghosts/", dead = prefix + "dead.png";

    private static final ImageIcon deadImage = new ImageIcon(dead);
    private static final long pause = 2000L;
    private final ImageIcon[] images;
    private final Cell[][] cells;
    private final Board board;

    private final Random randGen = new Random();


    private final int order, defaultRow, defaultCol;

    private int row, col;

    private boolean isRestarting = false, isFree = false;

    private Directions direction = Directions.UP;

    protected boolean isActive = true, isDead = false;


    public Ghost(Board b, int r, int c, GhostsType t, int ord){
        board = b;
        defaultRow = r;
        defaultCol = c;
        setDefaultPosition();
        String[] files = new String[]{prefix + t + "_up.png", prefix + t + "_down.png",
                prefix + t + "_left.png", prefix + t + "_right.png"};
        images = new ImageIcon[files.length];
        for (int i = 0; i < files.length; i++){
            images[i] = new ImageIcon(files[i]);
        }
        cells = board.getCells();
        order = ord;
        start();
    }

    public void setDirection(Directions dir) {
        direction = dir;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getRow() {
        return row;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setDefaultPosition(){
        row = defaultRow;
        col = defaultCol;
        isFree = false;
        isRestarting = true;
        isDead = false;
    }

    public int getCol() {
        return col;
    }

    public void generateUpgrade(){
        switch (randGen.nextInt(5) + 1) {
            case(1) -> board.ups.add(new Apple(board, Upgrades.APPLE));
            case(2) -> board.ups.add(new Cherry(board, Upgrades.CHERRY));
            case(3) -> board.ups.add(new Strawberry(board, Upgrades.STRAWBERRY));
            case(4) -> board.ups.add(new Melon(board, Upgrades.MELON));
            case(5) -> board.ups.add(new Orange(board, Upgrades.ORANGE));
        }
    }

    public int colInBounds(int c){
        int col = c;
        if (c < 0) col = board.cols - 1;
        else if (c >= board.cols) col = 0;
        return col;
    }

    public boolean isCellRunnable(int row, int col){
        return (cells[row][col].getType() == 'p' || cells[row][col].getType() == '0' || cells[row][col].getType() == 'b');
    }

    public boolean isTunnel(int row, int col){
        col = colInBounds(col);
        return cells[row][col].getType() == 't';
    }

    public void moveGhost(){
        if (!isFree){
            for (int i = 0; i < 2; i ++){
                row --;
                board.repaint();
            }
            isFree = true;
            if (cells[row][col].getType() == 'w'){
                row --;
                isFree = true;
            }
        }
        else{
            switch (randGen.nextInt(4) + 1){
                case (1) -> {
                    if (isCellRunnable(row - 1, col)) row --;
                    direction = Directions.UP;
                }
                case (2) -> {
                    if (isCellRunnable(row + 1, col)) row ++;
                    direction = Directions.DOWN;

                }
                case (3) -> {
                    if (col - 1 >= 0 && isCellRunnable(row , col - 1)) col --;
                    else if (isTunnel(row, col) || isTunnel(row, col - 1)) col = board.cols - 1;
                    direction = Directions.LEFT;

                }
                case (4) -> {
                    if (col + 1 < board.cols && isCellRunnable(row , col + 1)) col ++;
                    else if (isTunnel(row, col) || isTunnel(row, col + 1)) col = 0;
                    direction = Directions.RIGHT;

                }
            }
        }

    }


    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
        long deadTime = 0;
        try {
            sleep(order * pause);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (isActive){
            if (isRestarting){
                try {
                    sleep(3000L * order);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                isRestarting = false;
            }
            if (board.lives == 0){
                isActive = false;
                break;
            }
            moveGhost();
            if ((System.currentTimeMillis() - startTime) % 5000 <= 50){
                if (randGen.nextInt(4) + 1 == 1 && board.ups.size() < 5) generateUpgrade();
            }
            if ((System.currentTimeMillis() - deadTime) % 5000 <= 50){
                isDead = false;
                deadTime = 0;
            }
            try {
                if (isDead){
                    if (deadTime == 0) deadTime = System.currentTimeMillis();
                    Thread.sleep(80);
                }
                else Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void draw(){
        ImageIcon img = new ImageIcon();
        if (isDead) img = deadImage;
        else{
            switch (direction.ordinal()){
                case (0) -> img = images[0];
                case (1) -> img = images[1];
                case (2) -> img = images[2];
                case (3) -> img = images[3];
            }
        }
        JLabel jb = new JLabel(img);
        board.add(jb);

    }

}
