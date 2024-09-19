import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


public class StartingWindow extends JFrame implements ActionListener, Serializable, KeyListener{
    private static Font font;

    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font.ttf"));
        } catch (FontFormatException | IOException e) {
            System.out.println("Font file is not found");
            font = new Font("Italic", Font.PLAIN, 20);
        }
    }

    private final JLabel USER = new JLabel("USER"), SCORE = new JLabel("SCORE");

    private final JButton playButton = new JButton("New Game");
    private final JButton leaderBoardButton = new JButton("High Scores");
    private final JButton exitButton = new JButton("Exit");
    private final ArrayList<JButton> menuButtons = new ArrayList<>();
    private final JPanel menuPanel = new JPanel();
    private ArrayList<ScoreRecord> records = new ArrayList<>();
    private boolean isMenu = true;



    public StartingWindow() {
        setDefaultSettings();
        drawMenu();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == leaderBoardButton) {
            isMenu = false;
            drawScores();
        }
        else if (e.getSource() == playButton){
            dispose();
            BoardSize b = new BoardSize();
        }
        else if (e.getSource() == exitButton){
            dispose();
            System.exit(0);
        }
    }



    public void clearFrame(){
        getContentPane().removeAll();
    }

    public void setFont(JComponent comp, float size){
        comp.setFont(font.deriveFont(size));
    }

    public void setDefaultSettings(){

        menuButtons.add(playButton);
        menuButtons.add(leaderBoardButton);
        menuButtons.add(exitButton);
        menuButtons.forEach((JButton b) -> {
            b.addActionListener(this);
        });

        addKeyListener(this);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setFocusable(true);
        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setTitle("Pacman");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);

    }

    public void panelSettings(){

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBounds(getWidth() / 2 - 200, getHeight() / 5 + 25, 400, 180);
        menuPanel.setBackground(Color.BLACK);
        menuButtons.forEach((JButton b) -> {
            setFont(b, 20f);
            b.setMaximumSize(new Dimension(this.menuPanel.getWidth(), 60));
            b.setAlignmentX(0.5f);
            this.menuPanel.add(b, CENTER_ALIGNMENT);
        });
        add(menuPanel);
    }

    public void highScoresSettings(){
        USER.setBounds(getWidth() / 3 - 70, 10, 200, 50);
        SCORE.setBounds(2 * getWidth() / 3 - 80, 10, 200, 50);

        setFont(USER, 20f);
        setFont(SCORE, 20f);

        USER.setForeground(Color.PINK);
        SCORE.setForeground(Color.PINK);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(panel);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setBackground(Color.BLACK);
        panel.setBackground(Color.BLACK);

        scrollPane.setBounds(getWidth() / 3 - 60,USER.getY() + USER.getHeight() ,
                getWidth() - getWidth() / 3 + 60, getHeight() - USER.getY() - USER.getHeight() - 35);


        readObject();
        String[] users = new String[records.size()];
        String[] scores = new String[records.size()];
        if (records.isEmpty()){
            JLabel label = new JLabel("NO RECORDS YET");
            setFont(label, 20f);
            label.setForeground(Color.cyan);
            label.setBounds(getWidth() / 2 - 150, getHeight() / 2 - 150, 300, 300);
            add(label);
            return;
        }
        for (int i = 0; i < users.length; i++){
            users[i] = records.get(i).userName;
            scores[i] = String.valueOf(records.get(i).score);
        }

        JList<String> userList = new JList<>(users);
        JList<String> scoreList = new JList<>(scores);

        setFont(userList, 20f);
        setFont(scoreList, 20f);

        userList.setForeground(Color.CYAN);
        userList.setBackground(Color.BLACK);
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        scoreList.setForeground(Color.CYAN);
        scoreList.setBackground(Color.BLACK);
        scoreList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        panel.add(userList);
        panel.add(scoreList);
        add(scrollPane);
        add(USER);
        add(SCORE);

    }

    public void drawMenu(){
        clearFrame();
        panelSettings();
        setVisible(true);
        repaint();
        revalidate();
    }

    public void drawScores(){
        clearFrame();
        highScoresSettings();
        setVisible(true);
        repaint();
        revalidate();
    }

    public void readObject(){
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream("assets/Score.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            records = (ArrayList<ScoreRecord>) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (IOException | ClassNotFoundException ex) {
            System.out.println("File with scores is not found or problem with desirable class");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isMenu){
            isMenu = true;
            drawMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
