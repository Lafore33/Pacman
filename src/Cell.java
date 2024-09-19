import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Cell {

    private char type;
    private ImageIcon img;
    public Cell(char t) {
        type = t;
        if (type == 'p') img = new ImageIcon("assets/imgs/dot.png");
        else if (type == 'b') img = new ImageIcon("assets/imgs/bigDot.png");
    }

    public void drawCell(Board board) {
        switch (type) {
            case ('h') -> {
                JPanel panel = new JPanel();
                panel.setBackground(Color.BLUE);
                board.add(panel);

            }
            case ('0'), ('t'), ('e'), ('x') -> {
                JPanel panel = new JPanel();
                panel.setBackground(Color.BLACK);
                board.add(panel);

            }
            case ('w') -> {
                JPanel panel = new JPanel();
                panel.setBackground(Color.WHITE);
                board.add(panel);

            }
            case ('p'), ('b') -> {
                JLabel jb = new JLabel(img);
                board.add(jb);
            }
            default -> {}
        }

    }


    public char getType(){
        return type;
    }

    public void setType(char c){
        type = c;
    }

}