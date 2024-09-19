import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;




public class BoardWindow extends JFrame implements KeyListener {
    private static Font font;

    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font.ttf"));
        } catch (FontFormatException | IOException e) {
            System.out.println("Font file is not found");
            font = new Font("Italic", Font.PLAIN, 20);
        }
    }
    private final Board board;
    private final int height, width;
    private final JLabel scoreText = new JLabel("SCORE");
    protected JLabel score = new JLabel("0");
    protected JLabel timer = new JLabel("0.0");

    protected  JLabel[] attempts = new JLabel[3];
    public BoardWindow(String level){
        ImageIcon pacman = new ImageIcon("assets/imgs/pac.png");
        setLocationRelativeTo(null);

        attempts[0] = new JLabel(pacman);
        attempts[1] = new JLabel(pacman);
        attempts[2] = new JLabel(pacman);

        board = new Board(this, level);

        this.height = board.rows * 15;
        this.width = board.cols * 15;
        setDefaultSettings();

        setFont();
        addingComponents();
    }

    public void setDefaultSettings(){
        setSize(width, height + 2 * height / 5);
        setLocationRelativeTo(null);
        setTitle("Pacman");
        addKeyListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
    }

    public void setFont(JComponent comp, float size){
        comp.setFont(font.deriveFont(size));
    }

    public void addingComponents(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Add board
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(board, gbc);

        // Add scoreText
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        add(scoreText, gbc);

        // Add score
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        add(score, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx =  0.5;
        add(timer, gbc);


        // Add attempts
        JPanel attemptsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        attemptsPanel.setOpaque(false);
        for (JLabel attempt : attempts) {
            attemptsPanel.add(attempt);
        }
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        add(attemptsPanel, gbc);

    }

    public void setFont(){
        scoreText.setForeground(Color.WHITE);
        score.setForeground(Color.WHITE);
        timer.setForeground(Color.WHITE);
        setFont(timer, 15f);
        setFont(scoreText, 30f);
        setFont(score, 20f);
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        board.pacman.isMoving = true;
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_DOWN) -> board.pacman.setDirection(Directions.DOWN);
            case (KeyEvent.VK_UP) -> board.pacman.setDirection(Directions.UP);
            case (KeyEvent.VK_RIGHT) -> board.pacman.setDirection(Directions.RIGHT);
            case (KeyEvent.VK_LEFT) -> board.pacman.setDirection(Directions.LEFT);
            case (KeyEvent.VK_ESCAPE) -> {
                board.stopAll();
                board.removeAll();
                dispose();
                StartingWindow sw = new StartingWindow();}
            default -> board.pacman.isMoving = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
