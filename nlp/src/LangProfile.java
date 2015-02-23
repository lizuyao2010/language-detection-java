import java.util.HashMap;
import java.util.List;

/**
 * Created by lizuyao on 2/22/15.
 */
public class LangProfile {
    private String name;
    private HashMap<String,Long> freq;
    private List<Long> n_words;

    public LangProfile(String name, HashMap<String, Long> freq, List<Long> n_words) {
        this.name = name;
        this.freq = freq;
        this.n_words = n_words;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Long> getFreq() {
        return freq;
    }

    public List<Long> getN_words() {
        return n_words;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFreq(HashMap<String, Long> freq) {
        this.freq = freq;
    }

    public void setN_words(List<Long> n_words) {
        this.n_words = n_words;
    }
}
