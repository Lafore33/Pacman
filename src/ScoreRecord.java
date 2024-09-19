import java.io.Serializable;

public class ScoreRecord implements Serializable {
    public String userName;
    public int score;

    public ScoreRecord(String name, int points){
        userName = name;
        score = points;
    }

    @Override
    public String toString() {
        return userName + " " + score;
    }
}
