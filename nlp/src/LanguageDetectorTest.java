import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class LanguageDetectorTest {

    @Test
    public void testPredict() throws Exception {
        final String modelfile="nlp/lang"; // directory contains pretrained model
        LanguageDetector languageDetector =new LanguageDetector(modelfile);
        BufferedReader br = new BufferedReader(new FileReader("nlp/test/europarl.test"));
        String line=null;
        int correct=0;
        int total=0;
        while ( (line=br.readLine()) != null) {
            String[] example = line.trim().split("\t");
            String predict_label = languageDetector.predict(example[1], 2);
            if (predict_label.equals(example[0]))
                    correct++;
            total++;
        }
        System.out.println("Accuracy: "+(double)correct/total);
    }
}