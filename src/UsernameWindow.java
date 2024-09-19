import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UsernameWindow extends JFrame implements ActionListener, Serializable{
    private static Font font;

    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/font.ttf"));
        } catch (FontFormatException | IOException e) {
            System.out.println("Font file is not found");
            font = new Font("Italic", Font.PLAIN, 20);
        }
    }
    private final int width, height;
    private final Board board;
    private final JLabel result;
    private final JButton saveButton = new JButton("SAVE"), cancelButton = new JButton("CANCEL");
    private final JLabel entering = new JLabel("ENTER USERNAME");

    private ArrayList<ScoreRecord> records = new ArrayList<>();
    private final JTextArea input = new JTextArea();

    public UsernameWindow(int w, int h, Board b){
        width = w;
        height = h;
        setDefaultSettings();
        board = b;
        result = new JLabel("FINAL SCORE IS " + board.score);
        setFont();
        setBound();
        addComponents();

    }

    public void setDefaultSettings(){
        setSize(width, height);
        setLocationRelativeTo(null);
        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setLayout(null);
        setTitle("Pacman");
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
    }
    public void readObject(){
        boolean updated = false;
        FileInputStream fileIn;
        try {
            if (!new File("assets/Score.dat").exists()) {
                records.add(new ScoreRecord(input.getText(), board.score));
                return;
            }
            fileIn = new FileInputStream("assets/Score.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            records = (ArrayList<ScoreRecord>) in.readObject();
            // this cycle updates records with the same username if current record has more points
            // *not sure if it should be like that
            for (ScoreRecord record : records){
                if (record.userName.equals(input.getText())){
                    record.score = board.score;
                    updated = true;
                    break;
                }
            }
            if (!updated) records.add(new ScoreRecord(input.getText(), board.score));

            in.close();
            fileIn.close();
        }
        catch (IOException | ClassNotFoundException ex) {
            System.out.println("File with scores is not found");
        }
    }

    public void writeObject(){
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("assets/Score.dat");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(records);
            fileOut.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == saveButton){
            if (input.getText().isEmpty()) return;

            readObject();
            writeObject();
            dispose();
            StartingWindow sw = new StartingWindow();
        }
        else if (e.getSource() == cancelButton){
            dispose();
            StartingWindow sw = new StartingWindow();
        }
    }

    public void setFont(JComponent comp, float size){
        comp.setFont(font.deriveFont(size));
    }
    public void setBound(){
        entering.setBounds(width / 2 - 150, 2 * height / 5 - 30 , 600, 30);
        result.setBounds(width / 2 - 150, height / 5 - 15 , 600, 30);
        input.setBounds(width / 2 - 150, 3 * height / 5 - 15 , 300, 30);
        saveButton.setBounds(width / 3 - 75, 4 * height / 5 - 15, 150, 30);
        cancelButton.setBounds(2 * width / 3 - 75, 4 * height / 5 - 15, 150, 30);
    }

    public void setFont(){
        setFont(result, 20f);
        setFont(entering, 20f);
        setFont(cancelButton, 20f);
        setFont(input, 20f);
        setFont(saveButton, 20f);

    }
    public void addComponents(){
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);
        result.setForeground(Color.WHITE);
        entering.setForeground(Color.WHITE);
        input.setBackground(Color.WHITE);
        add(result);
        add(saveButton);
        add(cancelButton);
        add(input);
        add(entering);
    }


}
