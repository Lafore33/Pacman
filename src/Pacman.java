import javax.swing.*;

public class Pacman extends Thread{
    private static final String prefix = "assets/imgs/", closedState = prefix + "closed.png";
    private static final ImageIcon closedImage = new ImageIcon(closedState);
    private static final String[] filesU = {prefix + "pac_up.png", prefix + "pac_up_wide.png",
            prefix + "pac_up_widest.png", closedState};
    private static final String[] filesD = {prefix + "pac_down.png", prefix + "pac_down_wide.png",
            prefix + "pac_down_widest.png", closedState};
    private static final String[] filesR = {prefix + "pac_right.png", prefix + "pac_right_wide.png",
            prefix + "pac_right_widest.png", closedState};
    private static final String[] filesL = {prefix + "pac_left.png", prefix + "pac_left_wide.png",
            prefix + "pac_left_widest.png", closedState};
    private static final String[] filesE = {closedState, prefix + "pac_up.png", prefix + "erase1.png",
                                            prefix + "erase2.png",prefix + "erase3.png", prefix + "erase4.png",
                                            prefix + "erase5.png",prefix + "erase6.png", prefix + "erase7.png"};
    private final ImageIcon[] imagesU = new ImageIcon[filesU.length];
    private final ImageIcon[] imagesD = new ImageIcon[filesD.length];
    private final ImageIcon[] imagesR = new ImageIcon[filesR.length];
    private final ImageIcon[] imagesL = new ImageIcon[filesL.length];
    private final ImageIcon[] imagesE = new ImageIcon[filesE.length];
    private Cell[][] cells;
    private final Board board;
    private final int defaultRow, defaultCol;
    private int row, col, currentState = 0;
    private boolean isRestarting = false;
    private Directions direction = Directions.RIGHT;
    protected int velocity = 100, basicPoints = 10;
    protected boolean isMoving = false, isInvincible = false, isActive = true;


    public Pacman(Board b, int row, int col){
        board = b;
        defaultRow = row;
        defaultCol = col;
        setDefaultPosition();
        cells = board.getCells();
        createImages(filesD, imagesD);
        createImages(filesU, imagesU);
        createImages(filesR, imagesR);
        createImages(filesL, imagesL);
        createImages(filesE, imagesE);
        start();
    }

    public void setCells(Cell[][] cls){
        cells = cls;
    }

    private void setDefaultPosition(){
        row = defaultRow;
        col = defaultCol;
    }

    private static void createImages(String[] files, ImageIcon[] images){
        for (int i = 0; i < files.length; i++){
            images[i] = new ImageIcon(files[i]);
        }
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setVelocity(int vel){
        velocity = vel;
    }

    public void setDirection(Directions dir) {
        if (isRestarting || board.lives == 0) return;
        if (dir == Directions.ERASING) {isRestarting = true; currentState = 0;}
        direction = dir;
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

    public void isEatable(int row, int col){
        if (cells[row][col].getType() == 'b'){
            board.score += 50;
            for (Ghost g : board.ghosts){
                if (g.isFree()) g.isDead = true;
            }
            cells[row][col].setType('0');
        } else if (cells[row][col].getType() == 'p') {
            board.score += basicPoints;
            cells[row][col].setType('0');
        }
    }

    public void movePacman(){
        if (!isMoving || isRestarting) {currentState++; return;}
        switch (direction.ordinal()){
            case (0) -> {
                if (isCellRunnable(row - 1, col)) row --;
            }
            case (1) -> {
                if (isCellRunnable(row + 1, col)) row ++;
            }
            case (2) -> {
                if (col - 1 >= 0 && isCellRunnable(row , col - 1)) col --;
                else if (isTunnel(row, col) || isTunnel(row, col - 1)) col = board.cols - 1;
            }
            case (3) -> {
                if (col + 1 < board.cols && isCellRunnable(row , col + 1)) col ++;
                else if (isTunnel(row, col) || isTunnel(row, col + 1)) col = 0;
            }
        }
        currentState++;
        isEatable(row, col);
    }
    public void checkCollision(){
        if (isInvincible) {
            isInvincible = false;
        }
        else{
            for (Ghost g : board.ghosts){
                if (getRow() == g.getRow() && getCol() == g.getCol()){
                    if (g.isDead) g.setDefaultPosition();
                    else board.loseLife();
                }
            }
        }
    }


    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
        while (isActive){
            if (isRestarting && currentState == 0){
                // pause before dying animation
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
            if (board.lives == 0 && !isRestarting) {board.gameOver();break;}
            movePacman();
            checkCollision();
            SwingUtilities.invokeLater(() -> {
                board.removeAll();
                board.drawComp();
                float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
                board.bw.timer.setText(String.format("%.1f", elapsedTime) + "s");
                board.bw.score.setText(Integer.toString(board.score));
                board.repaint();
                board.revalidate();
            });

            try {
                Thread.sleep(velocity);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    public void draw(){
        ImageIcon img = new ImageIcon();
        switch (direction.ordinal()){
            case (0) -> {
                if (currentState > filesU.length - 1){
                    img = closedImage;
                    currentState = 0;
                }
                else img = imagesU[currentState];
            }
            case (1) -> {
                if (currentState > filesD.length - 1){
                    img = closedImage;
                    currentState = 0;
                }
                else img = imagesD[currentState];
            }
            case (2) -> {
                if (currentState > filesL.length - 1){
                    img = closedImage;
                    currentState = 0;
                }
                else img = imagesL[currentState];
            }
            case (3) -> {
                if (currentState > filesR.length - 1){
                    img = closedImage;
                    currentState = 0;
                }
                else img = imagesR[currentState];
            }
            case (4) -> {
                if (currentState > filesE.length - 1){
                    currentState = 0;
                    setDefaultPosition();
                    isRestarting = false;
                    setDirection(Directions.RIGHT);
                    img = imagesR[currentState];
                    isMoving = false;
                }
                else img = imagesE[currentState];
            }
        }
        JLabel jl = new JLabel(img);
        board.add(jl);
    }

}
