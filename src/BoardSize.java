import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class BoardSize extends JFrame implements ActionListener{
    private static Font font;

    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font.ttf"));
        } catch (FontFormatException | IOException e) {
            System.out.println("Font file is not found");
            font = new Font("Italic", Font.PLAIN, 20);
        }
    }
    private final JButton startGame = new JButton("Start"), cancel = new JButton("Cancel");
    private final JScrollPane levelsScrollPane;
    private final JButton[] levels = {new JButton("1"), new JButton("2"),new JButton("3"),
            new JButton("4"),new JButton("5")};

    private boolean showLevel = false;
    private JButton level = new JButton();


    public BoardSize(){
        setDefaultSettings();
        JPanel panel1 = new JPanel(new GridLayout(0, 1));


        for (JButton but : levels){
            panel1.add(but);
        }

        levelsScrollPane = new JScrollPane(panel1);

        level = new JButton(levels[0].getText());

        levelsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        levelsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        setBounds();

        addListeners();
        setVisible(true);
        addComponents();
    }

    public void setDefaultSettings(){
        setSize(300, 200);
        setLocationRelativeTo(null);

        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);
    }
    public void addComponents(){
        add(startGame);
        add(cancel);
        add(level);
    }

    public void addListeners(){


        for (JButton but : levels){
            but.addActionListener(this);
        }
        cancel.addActionListener(this);
        startGame.addActionListener(this);
        level.addActionListener(this);
    }

    public void setBounds(){
        setFont();
        startGame.setBounds(getWidth() / 4 - 40, getHeight() - 50, 80, 20);
        cancel.setBounds(3 * getWidth() / 4 - 40, getHeight() - 50, 80, 20);
        level.setBounds(5 * getWidth() / 12 - 10, getHeight() / 2 - 12, getWidth() / 6 + 20, 25);
        levelsScrollPane.setBounds(level.getX() - 2, level.getY() - 30, getWidth() / 6 + 25, 30);

    }

    public void setFont(JComponent comp, float size){
        comp.setFont(font.deriveFont(size));
    }

    public void setFont(){

        for (JButton temp : levels){
            setFont(temp, 10f);

        }
        setFont(startGame, 10f);
        setFont(cancel, 10f);
        setFont(level, 10f);
        setFont(levelsScrollPane, 10f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel){
            dispose();
            StartingWindow w = new StartingWindow();
        }

        else if (e.getSource() == level){
            showLevel = !showLevel;
            if (showLevel) add(levelsScrollPane);
            else getContentPane().remove(levelsScrollPane);
        }
        else if (e.getSource() == startGame){
            dispose();
            BoardWindow bw = new BoardWindow(level.getText());
        }

        for (JButton but : levels){
            if (e.getSource() == but) level.setText(but.getText());
        }
        repaint();
    }

}


