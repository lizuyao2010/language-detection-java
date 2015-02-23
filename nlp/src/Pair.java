import java.util.Objects;

/**
 * Created by lizuyao on 2/22/15.
 */
public class Pair implements Comparable{
    private double score;
    private String label;

    public Pair(double score, String label) {
        this.score = score;
        this.label = label;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int compareTo(Object o) {
        Pair other = (Pair) o;
        return (int)-(this.score - other.score);
    }
}
