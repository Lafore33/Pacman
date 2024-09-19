import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board extends JPanel{

    protected ArrayList<Upgrade> ups = new ArrayList<>();

    protected Pacman pacman;
    protected ArrayList<Ghost> ghosts;
    protected int score = 0, lives = 3, rows, cols;
    protected BoardWindow bw;

    protected String board;

    protected Cell[][] cells;

    private final String level;


    public Board(BoardWindow boardWindow, String l){
        bw = boardWindow;
        level = l;
        board = "assets/levels/level" + level + ".txt";
        createCells(board);
        createGhosts();
        createPacman();
        setLayout(new GridLayout(rows, cols));
        drawComp();
    }


    private void createPacman(){
        switch (level) {
            case "1" -> pacman = new Pacman(this, 26, 16);
            case "2" -> pacman = new Pacman(this, 20, 29);
            case "3" -> pacman = new Pacman(this, 13, 30);
            case "4" -> pacman = new Pacman(this, 17, 14);
            default -> pacman = new Pacman(this, 27, 38);
        }
    }
    private void createGhosts(){
        Ghost inky;
        Ghost blinky;
        Ghost clyde;
        Ghost pinky;
        switch (level) {
            case "1" -> {
                inky = new Ghost(this, 22, 13, GhostsType.Inky, 1);
                blinky = new Ghost(this, 22, 16, GhostsType.Blinky, 2);
                clyde = new Ghost(this, 23, 13, GhostsType.Clyde, 3);
                pinky = new Ghost(this, 23, 16, GhostsType.Pinky, 4);
            }
            case "2" -> {
                inky = new Ghost(this, 13, 30, GhostsType.Inky, 1);
                blinky = new Ghost(this, 13, 33, GhostsType.Blinky, 2);
                clyde = new Ghost(this, 14, 30, GhostsType.Clyde, 3);
                pinky = new Ghost(this, 14, 33, GhostsType.Pinky, 4);
            }
            case "3" -> {
                inky = new Ghost(this, 9, 28, GhostsType.Inky, 1);
                blinky = new Ghost(this, 9, 30, GhostsType.Blinky, 2);
                clyde = new Ghost(this, 10, 28, GhostsType.Clyde, 3);
                pinky = new Ghost(this, 10, 30, GhostsType.Pinky, 4);
            }
            case "4" -> {
                inky = new Ghost(this, 13, 14, GhostsType.Inky, 1);
                blinky = new Ghost(this, 13, 15, GhostsType.Blinky, 2);
                clyde = new Ghost(this, 14, 14, GhostsType.Clyde, 3);
                pinky = new Ghost(this, 14, 15, GhostsType.Pinky, 4);
            }
            default -> {
                inky = new Ghost(this, 20, 38, GhostsType.Inky, 1);
                blinky = new Ghost(this, 20, 41, GhostsType.Blinky, 2);
                clyde = new Ghost(this, 21, 38, GhostsType.Clyde, 3);
                pinky = new Ghost(this, 21, 41, GhostsType.Pinky, 4);
            }
        }
        ghosts = new ArrayList<>();
        ghosts.add(inky);
        ghosts.add(blinky);
        ghosts.add(pinky);
        ghosts.add(clyde);

    }

    protected void createCells(String path){

        Scanner reader;
        String line;
        ArrayList<String> lines = new ArrayList<>();

        try {
            reader = new Scanner(new File(path));
            while (true){
                line = null;
                try {
                    line = reader.nextLine();
                }
                catch (Exception eof){
                    //
                }
                if (line == null){
                    break;
                }
                lines.add(line);
            }
            rows = lines.size();
            cols = lines.get(0).length();
            cells = new Cell[rows][cols];
            for (int i = 0; i < rows; i++){
                for (int j = 0; j < cols; j++){
                    cells[i][j] = new Cell(lines.get(i).charAt(j));
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("assets/level" + level + ".txt is not found");}

    }
    public Cell[][] getCells(){
        return cells;
    }

    private boolean isEnd(){
        for (Cell[] row : cells){
            for (Cell cell : row){
                if (cell.getType() == 'p' || cell.getType() == 'b'){
                    return false;
                }
            }
        }
        return true;
    }

    public void stopAll(){
        ghosts.forEach((Ghost g) -> g.isActive = false);
        ups.forEach((Upgrade up) -> up.isActive = false);
        pacman.isActive = false;
    }
    public void loseLife(){
        pacman.setDirection(Directions.ERASING);

        createGhosts();
        ghosts.forEach((Ghost g) -> {
            g.setDefaultPosition();
            g.setDirection(Directions.UP);
        });

        ups.forEach((Upgrade up) -> up.isActive = false);
        ups.clear();


        if (lives == 0){return;}
        lives --;
        bw.attempts[lives].setVisible(false);
    }

    public void drawCells(){
        setLayout(new GridLayout(rows, cols));
        setBackground(Color.BLACK);
        boolean isDrawn;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == pacman.getRow() && j == pacman.getCol()) pacman.draw();
                else {
                    isDrawn = false;
                    for (Ghost g : ghosts){
                        if (i == g.getRow() && j == g.getCol()){
                            g.draw();
                            isDrawn = true;
                            break;
                        }
                    }
                    if (!isDrawn){
                        for (Upgrade up : ups){
                            if (i == up.getRow() && j == up.getCol()){
                                isDrawn = true;
                                up.draw();
                                break;
                            }
                        }
                    }
                    if (!isDrawn) cells[i][j].drawCell(this);
                }

            }
        }

    }

    void drawComp(){
        if (isEnd()){
            pacman.setDirection(Directions.ERASING);

            ghosts.forEach((Ghost g) -> {
                g.setDefaultPosition();
                g.setDirection(Directions.UP);
            });

            ups.forEach((Upgrade up) -> up.isActive = false);
            ups.clear();

            createCells(board);
            pacman.setCells(cells);
        }
        drawCells();

    }
    public void gameOver(){
        stopAll();
        bw.dispose();
        UsernameWindow wd = new UsernameWindow(1000, 500, this);
    }

}




